package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exceptions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/*
   Class Server used to perform an entire game of Santorini with clients connected to the server
   Needs to be completed with the game lobby, management of connections and disconnections, and setting of Active/ Inactive Players
 */

public class Server implements Runnable {

    private final Object clientsLock = new Object();
    private final Socket socket = new Socket();
    private static Map<String, ServerConnection> clients;
    private int socketPort;
    public static final Logger LOGGER = Logger.getLogger("Server");
    private Message message;
    private ObjectInputStream in;


    private static Game game;
    private static Controller controller;
    private static VirtualView virtualView;


    private void initLogger() {
        Date date = GregorianCalendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM_HH.mm.ss");

        try {
            FileHandler fh = new FileHandler("log/server-" + dateFormat.format(date) + ".log");
            fh.setFormatter(new SimpleFormatter());

            LOGGER.addHandler(fh);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void startServer() {
        SocketServer serverSocket = new SocketServer(this, socketPort);
        serverSocket.start();

        LOGGER.info("Socket Server Started");
    }


    /**
     * Login method Adds player to the server
     */
    static void login(String username, ServerConnection connection) {
        clients.put(username, connection);
        LOGGER.log(Level.INFO, "{0} connected to server!", username);
    }

    /**
     * The sendMessage method sends a message to client
     *
     * @param username username of the client who will receive the message
     * @param message  message to send
     */
    public void sendMessage(String username, Message message) {
        synchronized (clientsLock) {
            for (Map.Entry<String, ServerConnection> client : clients.entrySet()) {
                if (client.getKey().equals(username) && client.getValue() != null && client.getValue().isActive()) {
                    try {
                        client.getValue().sendServerMessage(message);
                    } catch (IOException e) {
                        LOGGER.severe(e.getMessage());
                    }
                    break;
                }
            }
        }

        LOGGER.log(Level.INFO, "Send: {0}, {1}", new Object[]{username, message});

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                message = (Message) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        }
    }

    /**
     * The listen method return the message sent to server
     */
    public String listen () {
        return message.getContent();
    }

    public static Game getGame() {
        Game g = game;
        return g;
    }



    /**
     * The Server method starts with a new game
     */
    public Server() throws IOException, InvalidInputException, WorkerNotExistException {
        initLogger();
        synchronized (clientsLock) {
            clients = new HashMap<>();
        }
        startServer();
        in = new ObjectInputStream(socket.getInputStream());


        virtualView = new VirtualView();
        controller = new Controller(virtualView);
        virtualView.setController(controller);
        virtualView.setServer(this);
        controller.setServer(this);

        //    START OF THE COMMUNICATION WITH THE CLIENTS TO GET THE GAME'S SETTINGS

        //Insertion of Game Settings and Creation of the Game
        while (getGame() == null) {
            //Need to insert the message receiver/sender(USER)
            String USER = "";
            int numPlayers = 0;
            boolean withGods = false;
            String m1 = "Please insert the number of Players";
            CommunicationEvent event = new CommunicationEvent(USER,m1);
            game.notify(event);

            try {
                numPlayers = controller.receiveNumPlayersSelectedEvent(USER);
            } catch (InvalidSenderException e) {
                InvalidInputEvent ev = new InvalidInputEvent(USER, e);
                game.notify(ev);
            }

            if (numPlayers == 2) {

                String m2 = "Do you want to play with gods?";
                //Need to insert the message receiver(Only Connected User)
                CommunicationEvent event2 = new CommunicationEvent(USER,m2);
                game.notify(event2);
                try {
                    withGods = controller.receiveWithGodsSelectedEvent(USER);
                    game = new Game(numPlayers, withGods);
                    game.setVirtualView(virtualView);
                    controller.setGame(game);
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(USER, e);
                    game.notify(ev);
                }


            }
            if (numPlayers == 3) {
                withGods = true;
                game = new Game(numPlayers, withGods);
                game.setVirtualView(virtualView);
                controller.setGame(game);

            } else {
                InvalidInputEvent ev = new InvalidInputEvent(USER, new InvalidInputException());
                game.notify(ev);

            }


        }

        game.gameUpdate();


        //Players enter the game until the game is full
        while (game.isGameFull()) {
            for (int i = 0; i < game.getNumPlayers(); i++) {
                String ID = null;
                int age = 0;
                PlayerColor color = null;
                String m3 = "Please insert your data, colors available are: " + game.getAvailableColors();

                CommunicationEvent event = new CommunicationEvent(game.getIDs().get(i),m3);
                game.notify(event);

                try {
                    Object[] data = controller.receivePlayerDataEnteredEvent(game.getIDs().get(i));
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(i), e);
                    game.notify(ev);
                }


                try {
                    game.addPlayer(ID, age, color);
                    PlayerJoinedEvent event2 = new PlayerJoinedEvent(game.getIDs().get(i), getGame());
                    game.notify(event2);

                } catch (InvalidInputException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(i), e);
                    game.notify(ev);
                }
            }

        }
        game.gameUpdate();

        if (game.isWithGods()) {

            //Selection of God Cards
            boolean cardsAssigned = false;
            while (!cardsAssigned) {
                ArrayList<String> cards = new ArrayList<>();

                String m4 = "Please select the cards, gods available are: " + game.availableGods;
                CommunicationEvent event = new CommunicationEvent(game.getIDs().get(0),m4);
                game.notify(event);

                try {
                    cards = controller.receiveGodsSelectedEvent(game.getIDs().get(0));
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0), e);
                    game.notify(ev);
                }

                try {
                    game.selectGodCards(cards);
                    cardsAssigned = true;
                    GodCardsSelectedEvent event3 = new GodCardsSelectedEvent(game.getIDs().get(0), getGame());
                    game.notify(event3);
                } catch (InvalidInputException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0), e);
                    game.notify(ev);
                }
            }

            //Each player chooses its card
            for (int j = game.getNumPlayers() - 1; j > 0; j--) {
                boolean done = false;
                while (!done) {
                    String m5 = "Select your Card" + "Cards available are: " + game.getGods();
                    CommunicationEvent event = new CommunicationEvent(game.getIDs().get(j),m5);
                    game.notify(event);


                    String card = null;
                    try {
                        card = controller.receiveCardSelectedEvent(game.getIDs().get(j));
                    } catch (InvalidSenderException e) {
                        InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(j), e);
                        game.notify(ev);
                    }

                    try {
                        game.chooseCard(game.getPlayers().get(j), card);
                        done = true;
                        CardSelectionEvent event2 = new CardSelectionEvent(game.getIDs().get(j), card);
                        game.notify(event2);

                    } catch (InvalidInputException e) {
                        InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(j), e);
                        game.notify(ev);

                    }
                }
            }

            //First Player gets the remaining card
            String card = game.getGods().get(0);
            game.chooseCard(game.getPlayers().get(0), card);
            CardSelectionEvent event = new CardSelectionEvent(game.getIDs().get(0), card);
            game.notify(event);


            //Selection of the Starting Player
            boolean control = false;
            while (!control) {
                String starterID = null;
                String m6 = "Select the Starter " + game.getIDs();
                CommunicationEvent event3 = new CommunicationEvent(game.getIDs().get(0),m6);
                game.notify(event3);

                try {
                    starterID = controller.receiveStarterSelectedEvent(game.getIDs().get(0));
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0), e);
                    game.notify(ev);
                }

                for (int k = 0; k < game.getNumPlayers(); k++) {
                    if (game.getPlayers().get(k).getPlayerID().equalsIgnoreCase(starterID)) {

                        try {
                            game.chooseStarter(game.getPlayers().get(k));
                            control = true;
                            StarterSelectionEvent ev = new StarterSelectionEvent(game.getIDs().get(0), getGame());
                            game.notify(event);

                        } catch (InvalidInputException e) {
                            InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0), e);
                            game.notify(ev);
                        }
                    }
                }
            }

        }

        //Game without gods ----> starts the youngest player
        if (!game.isWithGods()) {
            int min = game.getMinAge();
            for (int i = 0; i < game.getNumPlayers(); i++) {
                if (game.getPlayers().get(i).getPlayerAge() == min) {
                    game.chooseStarter(game.getPlayers().get(i));
                    StarterSelectionEvent event = new StarterSelectionEvent(game.getIDs().get(0), getGame());
                    game.notify(event);

                    break;
                }
            }
        }



        //***** PLACEMENT OF WORKERS *******//

        for(int j = 0; j < game.getPlayers().size(); j++){
            boolean checkPlacement = false;
            Player player = game.getPlayers().get(j);
            String ID = player.getPlayerID();
            while(!checkPlacement){
                Box box1 = null;
                Box box2 = null;
                String m7 = "Please Select the first box";
                CommunicationEvent event3 = new CommunicationEvent(ID,m7);
                game.notify(event3);
                try {
                    box1 = controller.receiveBoxSelectedEvent(ID);
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(ID, e);
                    game.notify(ev);
                }

                String m8 = "Please Select the second box";
                CommunicationEvent event4 = new CommunicationEvent(ID,m8);
                game.notify(event4);
                try {
                    box2 = controller.receiveBoxSelectedEvent(ID);
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(ID, e);
                    game.notify(ev);
                }

                try{
                    game.placeWorkers(player,box1,box2);
                } catch (InvalidBoxException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(ID, e);
                    game.notify(ev);
                }

            }

        }





        //******** START OF THE GAME **********//


        //Starts the turns alternation between players
        match:
        while (game.getPlayers().size() > 1) {
            for (int j = 0; j < game.getPlayers().size(); j++) {
                boolean checkMovement = false;
                boolean checkConstruction = false;

                Player player = game.getPlayers().get(j);
                String ID = player.getPlayerID();

                if (!player.canMove()) {
                    game.removePlayer(player);
                    LoserPlayerEvent event = new LoserPlayerEvent(player.getPlayerID());
                    game.notify(event);
                    game.gameUpdate();
                }

                Worker selectedWorker = null;

                while (selectedWorker == null) {
                    String m7 = "Please Select one worker";
                    CommunicationEvent event3 = new CommunicationEvent(ID,m7);
                    game.notify(event3);

                    try {
                        selectedWorker = controller.receiveWorkerSelectedEvent(ID);
                    } catch (InvalidSenderException e) {
                        InvalidInputEvent ev = new InvalidInputEvent(ID, e);
                        game.notify(ev);
                    }

                }

                while (!checkMovement) {
                    //****** 3 SWITCH CASES (Standard, Prometheus, Arthemis) *****//


                    if (player instanceof PlayerArthemis) {
                        if(askForSpecialAction(ID)){
                            checkMovement = moveArthemis(player,ID,selectedWorker);
                        }else{
                            checkMovement = standardMove(player,ID,selectedWorker);
                        }

                    }
                    if (player instanceof PlayerPrometheus) {
                        if(askForSpecialAction(ID)){
                            checkMovement = movePrometheus(player,ID,selectedWorker);
                        }else{
                            checkMovement = standardMove(player,ID,selectedWorker);
                        }

                    }

                    else{
                        checkMovement = standardMove(player,ID,selectedWorker);
                    }

                }



                if (player.isWinner()) {
                    WinnerPlayerEvent event = new WinnerPlayerEvent(player.getPlayerID());
                    game.notify(event);

                    for (int i = 0; i < game.getIDs().size(); i++) {

                        if (!game.getIDs().get(i).equals(ID)) {
                            LoserPlayerEvent e = new LoserPlayerEvent(game.getIDs().get(i));
                            game.notify(event);
                        }
                    }
                    break match;
                }


                if (!player.canBuild()) {
                    LoserPlayerEvent event = new LoserPlayerEvent(player.getPlayerID());
                    game.notify(event);
                    game.removePlayer(player);
                    game.gameUpdate();
                }


                while (!checkConstruction) {
                    //****** 4 SWITCH CASES (Standard, Ephaestus, Demeter, Atlas) *****//


                    if (player instanceof PlayerAtlas) {
                        if(askForSpecialAction(ID)){
                            checkConstruction = buildAtlas(player,ID,selectedWorker);
                        }else{
                            checkConstruction = standardBuild(player,ID,selectedWorker);
                        }

                    }
                    if (player instanceof PlayerEphaestus) {
                        if(askForSpecialAction(ID)){
                            checkConstruction = buildEphaestus(player,ID,selectedWorker);
                        }else{
                            checkConstruction = standardBuild(player,ID,selectedWorker);
                        }

                    }

                    if (player instanceof PlayerDemeter) {
                        if(askForSpecialAction(ID)){
                            checkConstruction = buildDemeter(player,ID,selectedWorker);
                        }else{
                            checkConstruction = standardBuild(player,ID,selectedWorker);
                        }

                    }

                    else{
                        checkConstruction = standardBuild(player,ID,selectedWorker);
                    }

                }

            }

        }


        String goodbye = "Game over, thanks for playing Santorini!";
        for (int i = 0; i < game.getIDs().size(); i++) {
            CommunicationEvent event = new CommunicationEvent(game.getIDs().get(i), goodbye);
            game.notify(event);
        }
    }


    public static void main(String[] args) throws InvalidInputException, WorkerNotExistException, IOException {

        new Server();

    }




    // AUXILIARY METHODS USED WHEN PERFORMING A PLAYER'S TURN DURING THE GAME

    //Method used to ask for the special action of a player's god
    public static boolean askForSpecialAction(String ID){
        boolean gotResponse = false;
        while (!gotResponse) {
            String m = "Do you want to use your God special power?";
            CommunicationEvent event = new CommunicationEvent(ID,m);
            game.notify(event);

            boolean response;
            try {
                response = controller.receiveUseofSpecialPowerEvent(ID);
                gotResponse = true;
                return response;
            } catch (InvalidSenderException e) {
                InvalidInputEvent ev = new InvalidInputEvent(ID, e);
                game.notify(ev);
            }

        }
        return false;
    }

    //Standard Player Movement
    public static boolean standardMove(Player player, String ID, Worker selectedWorker) {
        String m8 = "Please Select one box";
        CommunicationEvent event = new CommunicationEvent(ID,m8);
        game.notify(event);

        Box box = null;
        try {
            box = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }

        try {
            player.move(selectedWorker, box);
            game.gameUpdate();
            return true;

        } catch (InvalidMovementException | AthenaConditionException | WrongMovementException | WorkerNotExistException | InvalidIndicesException e) {
            InvalidMovementEvent event2 = new InvalidMovementEvent(ID);
            game.notify(event2);
            return false;
        }
    }


    //Standard Player Construction
    public static boolean standardBuild(Player player, String ID, Worker selectedWorker) {
        String m9 = "Please Select one box";
        CommunicationEvent event = new CommunicationEvent(ID,m9);
        game.notify(event);

        Box box2 = null;
        try {
            box2 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }

        try {
            player.build(selectedWorker, box2);
            game.gameUpdate();
            return true;

        } catch (WrongConstructionException | InvalidConstructionException e) {
            InvalidConstructionEvent event2 = new InvalidConstructionEvent(ID);
            game.notify(event2);
            return false;

        }
    }



    //*****  AUXILIARY METHODS USED TO PERFORM DIVINITIES MOVEMENTS AND CONSTRUCTIONS**********

    //Method used to perform the special Arthemis Movement when required

    public static boolean moveArthemis(Player player, String ID, Worker selectedWorker){
        String m8 = "Please Select the first box";
        CommunicationEvent event = new CommunicationEvent(ID,m8);
        game.notify(event);

        Box box1 = null;
        try {
            box1 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }
        String m9 = "Please Select the second box";
        CommunicationEvent event2 = new CommunicationEvent(ID,m9);
        game.notify(event2);

        Box box2 = null;
        try {
            box2 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }

        try {
            PlayerArthemis player1 = (PlayerArthemis) player;
            player1.moveArthemis(box1, box2, selectedWorker);
            game.gameUpdate();
            return true;

        } catch (WrongMovementException e) {
            InvalidMovementEvent event6 = new InvalidMovementEvent(ID);
            game.notify(event6);
            return false;
        }

    }


    //Method used to perform the special Atlas Construction when required
    public static boolean buildAtlas(Player player, String ID, Worker selectedWorker){
        String m9 = "Please Select one box";
        CommunicationEvent event = new CommunicationEvent(ID,m9);
        game.notify(event);

        Box box2 = null;
        try {
            box2 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }

        try {
            PlayerAtlas player1 = (PlayerAtlas) player;
            player1.buildAtlas(box2, selectedWorker);
            game.gameUpdate();
            return true;

        } catch (WrongConstructionException e) {
            InvalidConstructionEvent event2 = new InvalidConstructionEvent(ID);
            game.notify(event2);
            return false;

        }

    }

    //Method used to perform the special Demeter Construction when required
    public static boolean buildDemeter(Player player, String ID, Worker selectedWorker){
        String m8 = "Please Select the first box";
        CommunicationEvent event = new CommunicationEvent(ID,m8);
        game.notify(event);

        Box box1 = null;
        try {
            box1 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }
        String m9 = "Please Select the second box";
        CommunicationEvent event2 = new CommunicationEvent(ID,m9);
        game.notify(event2);

        Box box2 = null;
        try {
            box2 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }

        try {
            PlayerDemeter player1 = (PlayerDemeter) player;
            player1.buildDemeter(selectedWorker, box1, box2);
            game.gameUpdate();
            return true;

        } catch (WrongConstructionException e) {
            InvalidMovementEvent event3 = new InvalidMovementEvent(ID);
            game.notify(event3);
            return false;
        }

    }



    //Method used to perform the special Ephaestus Construction when required
    public static boolean buildEphaestus(Player player, String ID, Worker selectedWorker){
        String m9 = "Please Select one box";
        CommunicationEvent event = new CommunicationEvent(ID,m9);
        game.notify(event);

        Box box2 = null;
        try {
            box2 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }

        try {
            PlayerEphaestus player1 = (PlayerEphaestus) player;
            player1.buildEphaestus(selectedWorker, box2);
            game.gameUpdate();
            return true;
        } catch (WrongConstructionException e) {
            InvalidConstructionEvent event2 = new InvalidConstructionEvent(ID);
            game.notify(event2);
            return false;
        }
    }


    //Method used to perform the special Prometheus Movement when required
    public static boolean movePrometheus(Player player, String ID, Worker selectedWorker){
        String m9 = "Please Select the first box";
        CommunicationEvent event = new CommunicationEvent(ID,m9);
        game.notify(event);

        Box box1 = null;
        try {
            box1 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
            return false;
        }
        String m10 = "Please Select the second box";
        CommunicationEvent event4 = new CommunicationEvent(ID,m10);
        game.notify(event4);

        Box box2 = null;
        try {
            box2 = controller.receiveBoxSelectedEvent(ID);
        } catch (InvalidSenderException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID, e);
            game.notify(ev);
        }


        try {
            Box startingBox = selectedWorker.getBox();
            int startingLevel = startingBox.getLevel();

            player.build(selectedWorker, box1);
            player.move(selectedWorker,box2);

            if(box2.getLevel()>startingLevel){
                box1.setLevel(box1.getLevel()-1);
                box1.setDome(false);
                player.move(selectedWorker, startingBox);
                throw new InvalidMovementException();
            }
            game.gameUpdate();
            return true;

        } catch (WrongConstructionException | InvalidConstructionException | InvalidMovementException | AthenaConditionException | WrongMovementException | WorkerNotExistException | InvalidIndicesException | NotValidLevelException e) {
            InvalidConstructionEvent event5 = new InvalidConstructionEvent(ID);
            game.notify(event5);
            return false;

        }


    }

}
