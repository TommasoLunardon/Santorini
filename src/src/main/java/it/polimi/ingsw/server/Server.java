package it.polimi.ingsw.server;

import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exceptions.*;


import java.io.*;
import java.net.ServerSocket;
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
   Needs to be completed with the management of "PING" Messages
 */

public class Server implements Runnable {

    private final Object clientsLock = new Object();
    private Socket socket = new Socket();
    private static Map<String, ServerConnection> clients;
    public static final int socketPort = 1111;
    public static final Logger LOGGER = Logger.getLogger("Server");
    private Message message;
    private ObjectInputStream in;


    private ArrayList<String> users;
    private static Controller controller;
    private static VirtualView virtualView;

    //Game used for multiple matches
    private static Game baseGame;


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
        try {
            ServerSocket serverSocket = new ServerSocket(socketPort);
            while (true) {
                serverSocket.accept();
                new Thread(this).start();
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void disconnect(String user){
        if (clients.get(user) != null) {
            LOGGER.log(Level.INFO, "{0} disconnect from server!", user);
            synchronized (clientsLock) {
                clients.remove(user);
            }
            LOGGER.log(Level.INFO, "{0} removed from client list!", user);
        }
    }


    /**
     * Login method Adds player to the server
     */
    public synchronized void login() {
        String user = virtualView.receiveConnectionRequest();

        clients.put(user, (ServerConnection) socket);
        this.users.add(user);
        LOGGER.log(Level.INFO, "{0} connected to server!", user);

        if (users.contains(user)){
            String message = "You need to change your username";
            CommunicationEvent event = new CommunicationEvent(user,message);
            virtualView.send(event);
            disconnect(user);
        }

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
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            message = (Message) in.readObject();
            input.close();
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                    Logger.getGlobal().warning(e.getMessage());
                }
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
        return controller.getGame();
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
        virtualView.setServer(this);

        //***** LOBBY *****//
        //Let up to three players connect to the server
        while(users.size() < 3){
            login();
        }
        controller.setUsers(users);


        //Message to players waiting for the game creation
        String message = "Please wait, the host is creating the game";
        for (int i = 1; i < users.size(); i++) {
            CommunicationEvent event = new CommunicationEvent(users.get(i), message);
            virtualView.send(event);
        }

        //Insertion of Game Settings and Creation of the Game
        controller.gameCreation();

        if(getGame().getNumPlayers() == 2){

            String m = "Sorry but the game is already full";
            CommunicationEvent event = new CommunicationEvent(users.get(2),m);
            virtualView.send(event);
            disconnect(users.get(2));

            controller.setUsers(users);

        }


        //Players enter the game until the game is full
        controller.playersEnter();

        //Base game for restarting the match
        baseGame =  getGame();

        boolean restart = true;
        //Starting Point for multiple matches with the same settings
        while(restart) {
            controller.setGame(baseGame);

            if (getGame().isWithGods()) {

                //Selection of God Cards
                controller.cardsSelection();

                //Each player chooses its card
                controller.godSelection();

                //First Player gets the remaining card
                String card = getGame().getGods().get(0);
                getGame().chooseCard(getGame().getPlayers().get(0), card);
                CardSelectionEvent event = new CardSelectionEvent(getGame().getIDs().get(0), card);
                virtualView.send(event);


                //Selection of the Starting Player
                controller.starterSelection();
            }

            //Game without gods ----> starts the youngest player
            if (!getGame().isWithGods()) {
                int min = getGame().getMinAge();
                for (int i = 0; i < getGame().getNumPlayers(); i++) {
                    if (getGame().getPlayers().get(i).getPlayerAge() == min) {
                        getGame().chooseStarter(getGame().getPlayers().get(i));
                        StarterSelectionEvent event = new StarterSelectionEvent(getGame().getIDs().get(0), getGame());
                        virtualView.send(event);

                        break;
                    }
                }
            }

            //Placement of Workers//
            controller.workersPlacement();

            //Starts the turns alternation between players
            controller.turns();


            String gameover = "Game over, thanks for playing Santorini!";
            for (int i = 0; i < getGame().getIDs().size(); i++) {
                CommunicationEvent event = new CommunicationEvent(getGame().getIDs().get(i), gameover);
                virtualView.send(event);
            }

            //Ask for another match
            boolean received = false;
            while (!received) {
                controller.setActive(users.get(0));
                String restartSelection = "Do you want to restart?";
                CommunicationEvent event = new CommunicationEvent(users.get(0), restartSelection);
                virtualView.send(event);

                try {
                    restart = virtualView.receiveRestartConfirmationEvent(users.get(0));
                    received = true;
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(users.get(0), e);
                    virtualView.send(ev);
                }
            }
        }
        //End of the Game all clients are disconnected
        String goodbye = "The host decided to conclude the matches";
        for (int i = 0; i < getGame().getIDs().size(); i++) {
            CommunicationEvent event = new CommunicationEvent(getGame().getIDs().get(i), goodbye);
            virtualView.send(event);
            disconnect(getGame().getIDs().get(i));
        }
    }


    public static void main(String[] args) throws InvalidInputException, WorkerNotExistException, IOException {

        Server server = new Server();
        server.startServer();

    }

}
