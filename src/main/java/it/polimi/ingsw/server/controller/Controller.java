package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exceptions.*;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Class Controller is assigned to a VirtualView and to a Model (GAME), it helps the server in performing the actions
 *  needed to play a Santorini Game.
 */
public class Controller implements Serializable {

    private Game game;
    private VirtualView view;
    private ArrayList<String> users;
    public  ArrayList<Player> removedPlayers = new ArrayList<>();

    public Game getGame(){
        Game g = game;
        return g;
    }

    public void setGame(Game g){
        this.game = g;
        game.setVirtualView(view);
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public Controller(VirtualView view){
        this.view = view;
        view.setController(this);
    }

    /**
     * Method used to get the game settings and create the game
     */
    public void gameCreation() throws SocketTimeoutException {
        while (getGame() == null) {
            String ID = users.get(0);
            int numPlayers = 0;
            boolean withGods;
            String active = "You are active now";
            CommunicationEvent activation = new CommunicationEvent(ID,active);
            view.send(activation);
            String m1 = "Please insert the number of Players";
            CommunicationEvent event = new CommunicationEvent(ID,m1);
            view.send(event);

            try {
                numPlayers = view.receiveNumPlayersSelectedEvent(ID);
            } catch (InvalidSenderException e) {
                InvalidInputEvent ev = new InvalidInputEvent(ID);
                view.send(ev);
            }

            if (numPlayers == 2) {

                String m2 = "Do you want to play with gods?";
                CommunicationEvent event2 = new CommunicationEvent(ID,m2);
                view.send(event2);
                try {
                    withGods = view.receiveWithGodsSelectedEvent(ID);
                    game = new Game(numPlayers, withGods);
                    game.setVirtualView(view);
                    game.gameUpdate();
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(ID);
                    game.notify(ev);
                }


            }
            if (numPlayers == 3) {
                game = new Game(numPlayers, true);
                game.setVirtualView(view);
                game.gameUpdate();

            }

        }
    }

    /**
     * Method used to insert all users in the game as players
     */
    public void playersEnter() throws SocketTimeoutException {
        while (!game.isGameFull()) {
            for (String user : users) {
                boolean entered = false;
                setActive(user);

                while (!entered) {
                    int age = 0;
                    PlayerColor color = null;
                    String m3 = "Please insert your data, colors available are: " + game.getAvailableColors();

                    CommunicationEvent event = new CommunicationEvent(user, m3);
                    game.notify(event);

                    try {
                        Object[] data = view.receivePlayerDataEnteredEvent(user);
                        age = (int) data[0];
                        color = (PlayerColor) data[1];
                    } catch (InvalidSenderException e) {
                        InvalidInputEvent ev = new InvalidInputEvent(user);
                        game.notify(ev);
                    }

                    try {
                        game.addPlayer(user, age, color);
                        PlayerJoinedEvent event2 = new PlayerJoinedEvent(user, game.getPlayers().get(game.getPlayers().size() - 1));
                        game.notify(event2);
                        entered = true;

                    } catch (InvalidInputException e) {
                        InvalidInputEvent ev = new InvalidInputEvent(user);
                        game.notify(ev);
                    }
                }
            }
        }
        game.gameUpdate();
    }

    /**
     * Method used to choose the god cards for the game
     */
    public void cardsSelection() throws SocketTimeoutException {
        boolean cardsAssigned = false;
        while (!cardsAssigned) {
            ArrayList<String> cards = new ArrayList<>();
            setActive(users.get(0));

            String m4 = "Please select the cards, gods available are: " + game.availableGods;
            CommunicationEvent event = new CommunicationEvent(game.getIDs().get(0),m4);
            game.notify(event);

            try {
                cards = view.receiveGodsSelectedEvent(game.getIDs().get(0));
            } catch (InvalidSenderException e) {
                InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0));
                game.notify(ev);
            }

            try {
                game.selectGodCards(cards);
                cardsAssigned = true;
                GodCardsSelectedEvent event3 = new GodCardsSelectedEvent(game.getIDs().get(0), getGame());
                game.notify(event3);
            } catch (InvalidInputException e) {
                InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0));
                game.notify(ev);
            }
        }
    }

    /**
     * Method used to assign a card to each player except the game creator
     */
    public void godSelection() throws SocketTimeoutException {
        for (int j = game.getNumPlayers() - 1; j > 0; j--) {
            boolean done = false;
            setActive(game.getIDs().get(j));
            while (!done) {
                String m5 = "Select your Card, " + "cards available are: " + game.getGods();
                CommunicationEvent event = new CommunicationEvent(game.getIDs().get(j),m5);
                game.notify(event);

                String card = null;
                try {
                    card = view.receiveCardSelectedEvent(game.getIDs().get(j));
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(j));
                    game.notify(ev);
                }

                try {
                    game.chooseCard(game.getPlayers().get(j), card);
                    done = true;
                    CardSelectionEvent event2 = new CardSelectionEvent(game.getIDs().get(j), card);
                    game.notify(event2);

                } catch (InvalidInputException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(j));
                    game.notify(ev);

                }
            }
        }
    }

    /**
     * Method used to select the starting player
     */
    public void starterSelection() throws SocketTimeoutException {
        boolean control = false;
        while (!control) {
            String starterID = null;
            setActive(game.getIDs().get(0));
            String m6 = "Select the Starter, players in the game are: " + game.getIDs();
            CommunicationEvent event3 = new CommunicationEvent(game.getIDs().get(0),m6);
            game.notify(event3);

            try {
                starterID = view.receiveStarterSelectedEvent(game.getIDs().get(0));
            } catch (InvalidSenderException e) {
                InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0));
                game.notify(ev);
            }

            for (int k = 0; k < game.getNumPlayers(); k++) {
                if (game.getPlayers().get(k).getPlayerID().equalsIgnoreCase(starterID)) {

                    try {
                        game.chooseStarter(game.getPlayers().get(k));
                        control = true;
                        StarterSelectionEvent ev = new StarterSelectionEvent(game.getIDs().get(0), getGame());
                        game.notify(ev);

                    } catch (InvalidInputException e) {
                        InvalidInputEvent ev = new InvalidInputEvent(game.getIDs().get(0));
                        game.notify(ev);
                    }
                }
            }
        }

    }

    /**
     * Method used to place the workers in the map
     */
    public void workersPlacement() throws SocketTimeoutException {
        for(int j = 0; j < game.getPlayers().size(); j++){
            boolean checkPlacement = false;
            Player player = game.getPlayers().get(j);
            String ID = player.getPlayerID();
            setActive(ID);

            while(!checkPlacement){
                Box box1 = null;
                Box box2 = null;
                String m = "Please Select the first box";
                try{
                    box1 = askForBox(ID,m);
                } catch (InvalidSenderException | InvalidIndicesException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(ID);
                    game.notify(ev);
                }

                String m2 = "Please Select the second box";
                try{
                    box2 = askForBox(ID,m2);
                } catch (InvalidSenderException | InvalidIndicesException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(ID);
                    game.notify(ev);
                }

                try{
                    game.placeWorkers(player,box1,box2);
                    game.gameUpdate();
                    checkPlacement = true;
                } catch (InvalidBoxException | InvalidInputException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(ID);
                    game.notify(ev);
                }

            }

        }

    }

    /**
     * Method used to perform the turns alternation between players
     * @throws InvalidInputException is never thrown because the player must belong to the game
     * @throws WorkerNotExistException if the worker doesn't belong to the game (Already checked when accepting the client's input)
     */
    public void turns() throws WorkerNotExistException, InvalidInputException, SocketTimeoutException {
        match:
        while (game.getPlayers().size() > 1) {
            for (int j = 0; j < game.getPlayers().size(); j++) {
                boolean checkMovement = false;
                boolean checkConstruction = false;

                Player player = game.getPlayers().get(j);
                String ID = player.getPlayerID();
                setActive(ID);

                if (!player.canMove()) {
                    LoserPlayerEvent event = new LoserPlayerEvent(ID);
                    game.notify(event);
                    game.removePlayer(player);
                    removedPlayers.add(player);
                    game.gameUpdate();
                    break;
                }

                Worker selectedWorker = null;

                while (selectedWorker == null) {
                    String m7 = "Please Select one worker";
                    CommunicationEvent event3 = new CommunicationEvent(ID,m7);
                    game.notify(event3);

                    try {
                        int selected = view.receiveWorkerSelectedEvent(ID);
                        selectedWorker = player.getWorkers().get(selected);
                    } catch (InvalidSenderException e) {
                        InvalidInputEvent ev = new InvalidInputEvent(ID);
                        game.notify(ev);
                    }

                }

                while (!checkMovement) {
                    //****** 3 SWITCH CASES (Standard, Prometheus, Arthemis) *****//

                    if (player instanceof PlayerArthemis) {
                        boolean check = askForSpecialAction(ID);
                        if(check){
                            checkMovement = moveArthemis(player,ID,selectedWorker);
                        }else{
                            checkMovement = standardMove(player,ID,selectedWorker);
                        }
                    }
                    else if (player instanceof PlayerPrometheus) {
                        boolean check = askForSpecialAction(ID);
                        if(check){
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
                    WinnerPlayerEvent event = new WinnerPlayerEvent(ID);
                    game.notify(event);

                    for (int i = 0; i < game.getIDs().size(); i++) {

                        if (!game.getIDs().get(i).equals(ID)) {
                            LoserPlayerEvent e = new LoserPlayerEvent(game.getIDs().get(i));
                            game.notify(e);
                        }
                    }
                    break match;
                }


                if (!player.canBuild()) {
                    LoserPlayerEvent event = new LoserPlayerEvent(player.getPlayerID());
                    game.notify(event);
                    game.removePlayer(player);
                    removedPlayers.add(player);
                    game.gameUpdate();
                    break;
                }


                while (!checkConstruction) {
                    //****** 4 SWITCH CASES (Standard, Ephaestus, Demeter, Atlas) *****//

                    if (player instanceof PlayerAtlas) {
                        boolean check = askForSpecialAction(ID);
                        if(check){
                            checkConstruction = buildAtlas(player,ID,selectedWorker);
                        }else{
                            checkConstruction = standardBuild(player,ID,selectedWorker);
                        }

                    }

                    else if (player instanceof PlayerDemeter) {
                        boolean check = askForSpecialAction(ID);
                        if(check){
                            checkConstruction = buildDemeter(player,ID,selectedWorker);
                        }else{
                            checkConstruction = standardBuild(player,ID,selectedWorker);
                        }

                    }

                    else if (player instanceof PlayerHephaestus) {
                        boolean check = askForSpecialAction(ID);
                        if(check){
                            checkConstruction = buildHephaestus(player,ID,selectedWorker);
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
    }

    // ****AUXILIARY METHODS USED WHEN PERFORMING A PLAYER'S TURN DURING THE GAME**** -----

    /**
     * Method used to ask for the special action of a player's god
     * @param ID is the username of the player to ask
     * @return true if the player asked for a special action
     */
    private boolean askForSpecialAction(String ID) throws SocketTimeoutException {
        boolean response = false;
        boolean gotResponse = false;
        while (!gotResponse) {
            String m = "Do you want to use your God special power?";
            CommunicationEvent event = new CommunicationEvent(ID,m);
            game.notify(event);

            try {
                response = view.receiveUseofSpecialPowerEvent(ID);
                gotResponse = true;
            } catch (InvalidSenderException e) {
                InvalidInputEvent ev = new InvalidInputEvent(ID);
                game.notify(ev);
            }

        }
        return response;
    }

    /**
     * Method used to perform the Standard Player Movement
     * @param player is the player performing the turn
     * @param ID is the player's ID
     * @param selectedWorker is the worker used to perform the turn
     * @return true if the movement was performed
     */
    private boolean standardMove(Player player, String ID, Worker selectedWorker) throws SocketTimeoutException {
        String m = "Please Select one box to move";
        Box box = null;
        try{
            box = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
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


    /**
     * Method used to perform the Standard Player Construction
     * @param player is the player performing the turn
     * @param ID is the player's ID
     * @param selectedWorker is the worker used to perform the turn
     * @return true if the construction was performed
     */
    private boolean standardBuild(Player player, String ID, Worker selectedWorker) throws SocketTimeoutException {
        String m = "Please Select one box to build";
        Box box = null;
        try{
            box = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }

        try {
            player.build(selectedWorker, box);
            game.gameUpdate();
            return true;

        } catch (WrongConstructionException | InvalidConstructionException e) {
            InvalidConstructionEvent event2 = new InvalidConstructionEvent(ID);
            game.notify(event2);
            return false;
        }
    }

    /**
     * Method used to perform the special Arthemis Movement when required
     * @param player is the player performing the turn
     * @param ID is the player's ID
     * @param selectedWorker is the worker used to perform the turn
     * @return true if the movement was performed
     */
    private boolean moveArthemis(Player player, String ID, Worker selectedWorker) throws SocketTimeoutException {
        String m = "Please Select one box to move";
        Box box1 = null;
        try{
            box1 = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }
        Box box2 = null;
        try{
            box2 = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }

        try {
            PlayerArthemis player1 = (PlayerArthemis) player;
            player1.moveArthemis(box1, box2, selectedWorker);
            game.gameUpdate();
            return true;

        } catch (WrongMovementException | AthenaConditionException | InvalidMovementException | WorkerNotExistException | InvalidIndicesException e) {
            InvalidMovementEvent event6 = new InvalidMovementEvent(ID);
            game.notify(event6);
            return false;
        }

    }

    /**
     * Method used to perform the special Atlas Construction when required
     * @param player is the player performing the turn
     * @param ID is the player's ID
     * @param selectedWorker is the worker used to perform the turn
     * @return true if the construction was performed
     */
    private boolean buildAtlas(Player player, String ID, Worker selectedWorker) throws SocketTimeoutException {
        String m = "Please Select one box to build";
        Box box = null;
        try{
            box = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }

        try {
            PlayerAtlas player1 = (PlayerAtlas) player;
            player1.buildAtlas(box, selectedWorker);
            game.gameUpdate();
            return true;

        } catch (WrongConstructionException e) {
            InvalidConstructionEvent event2 = new InvalidConstructionEvent(ID);
            game.notify(event2);
            return false;

        }

    }

    /**
     * Method used to perform the special Demeter Construction when required
     * @param player is the player performing the turn
     * @param ID is the player's ID
     * @param selectedWorker is the worker used to perform the turn
     * @return true if the construction was performed
     */
    private boolean buildDemeter(Player player, String ID, Worker selectedWorker) throws SocketTimeoutException {
        String m = "Please Select one box to build";
        Box box1 = null;
        try{
            box1 = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }

        Box box2 = null;
        try{
            box2 = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
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

    /**
     * Method used to perform the special Ephaestus Construction when required
     * @param player is the player performing the turn
     * @param ID is the player's ID
     * @param selectedWorker is the worker used to perform the turn
     * @return true if the construction was performed
     */
    private boolean buildHephaestus(Player player, String ID, Worker selectedWorker) throws SocketTimeoutException {
        String m = "Please Select one box to build";
        Box box = null;
        try{
            box = askForBox(ID,m);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }

        try {
            PlayerHephaestus player1 = (PlayerHephaestus) player;
            player1.buildHephaestus(selectedWorker, box);
            game.gameUpdate();
            return true;
        } catch (WrongConstructionException e) {
            InvalidConstructionEvent event2 = new InvalidConstructionEvent(ID);
            game.notify(event2);
            return false;
        }
    }

    /**
     * Method used to perform the special Prometheus Movement when required
     * @param player is the player performing the turn
     * @param ID is the player's ID
     * @param selectedWorker is the worker used to perform the turn
     * @return true if the movement was performed
     */
    private boolean movePrometheus(Player player, String ID, Worker selectedWorker) throws SocketTimeoutException {
        String m1 = "Please Select one box to build";
        Box box1 = null;
        try{
            box1 = askForBox(ID,m1);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }
        String m2 = "Please Select one box to move";
        Box box2 = null;
        try{
            box2 = askForBox(ID,m2);
        } catch (InvalidSenderException | InvalidIndicesException e) {
            InvalidInputEvent ev = new InvalidInputEvent(ID);
            game.notify(ev);
            return false;
        }
        try {
            PlayerPrometheus player1 = (PlayerPrometheus) player;
            player1.movePrometheus(box1, box2, selectedWorker);
            game.gameUpdate();
            return true;
        } catch (WrongConstructionException | InvalidConstructionException | PrometheusMovementException | NotValidLevelException e) {
            InvalidConstructionEvent event5 = new InvalidConstructionEvent(ID);
            game.notify(event5);
            return false;
        }

    }


    /**
     * Method used to communicate that a player is active, and all others are inactive
     * @param ID is the username that is active
     */
    public void setActive(String ID) throws SocketTimeoutException {
        String active = "You are active now";
        String inactive = "Please wait for the opponent to finish its action";

        CommunicationEvent activeEvent = new CommunicationEvent(ID, active);
        game.notify(activeEvent);
        for (String user : users) {
            if (!user.equals(ID)) {
                CommunicationEvent inactiveEvent = new CommunicationEvent(user, inactive);
                game.notify(inactiveEvent);
            }
        }

    }

    /**
     * Method used to ask the client to select a box
     * @param ID is the client's ID
     * @param message is the message sent to the client
     * @return the selected box
     * @throws SocketTimeoutException if the connection went into timeout
     * @throws InvalidSenderException if the sender is a wrong client
     * @throws InvalidIndicesException if the box's indices aren't valid
     */
    private Box askForBox(String ID, String message) throws SocketTimeoutException, InvalidSenderException, InvalidIndicesException {
        CommunicationEvent event = new CommunicationEvent(ID, message);
        game.notify(event);

        int[] received = view.receiveBoxSelectedEvent(ID);
        int x = received[0];
        int y = received[1];
        return game.getMap().getBox(x,y);

    }

}
