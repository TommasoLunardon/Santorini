package it.polimi.ingsw.server;

import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.network.server.SocketConnection;
import it.polimi.ingsw.network.server.SocketServer;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.exceptions.ClientNotFoundException;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exceptions.*;
import jdk.swing.interop.SwingInterOpUtils;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import java.util.stream.Collectors;


/*
   Class Server used to perform an entire game of Santorini with clients connected to the server
   Needs to be completed with the management of "PING" Messages
 */

public class Server implements Runnable {

    private final Object clientsLock = new Object();
    private static SocketServer socketServer;
    private static final int socketPort = 1111;
    public static final Logger LOGGER = Logger.getLogger("Server");

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
            System.out.println("ok");
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void startServer() {
        try {
            socketServer = new SocketServer(socketPort);
            socketServer.start();
            LOGGER.info("Socket Server Started");
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

    }

    public void disconnect(String user){
        if(!users.contains(user)){
            System.out.println("You are trying to disconnect a user that doesn't exist!");
        }else{
            socketServer.disconnect(user);
            users = socketServer.getUsers();
        }
    }

    /**
     * Login method Adds player to the server
     */
    public synchronized void login() {
        socketServer.run();
        users = socketServer.getUsers();
    }

    /**
     * The sendMessage method sends a message to client
     * @param username username of the client who will receive the message
     * @param message  message to send
     */
    public void sendMessage(String username, Message message) {
        synchronized (clientsLock) {
            if(users.contains(username)) {
                socketServer.sendMessage(username, message);
            }else{
                System.out.println("You are trying to reach a non existing user!");
            }
        }
        LOGGER.log(Level.INFO, "Send: {0}, {1}", new Object[]{username, message});
    }

    //Method used to receive messages from clients, it will continue to listen until will get a message.

    /**
     * //Method used to receive messages from clients, it will continue to listen until will get a message.
     * @return the received message
     */
    public String listen() {
        boolean received = false;
        String message = null;
        while(!received) {
            for (int i = 0; i < users.size(); i++) {
                try {
                    message = receive(users.get(i));
                    received = true;
                } catch (ClientNotFoundException e) {
                    System.out.println(); e.toString();
                }
            }
        }
        return message;
    }

    /**
     * The receive method return the message sent to server by a specific user
     */
    public String receive(String ID) throws ClientNotFoundException {
        try{
            return socketServer.receiveMessage(ID).getContent();
        } catch (IOException | ClassNotFoundException e) {
            throw new ClientNotFoundException();
        }
    }

    public static Game getGame() {
        return controller.getGame();
    }


    /**
     * The Server method starts with a new game
     */
    public Server() throws InvalidInputException, WorkerNotExistException {
        initLogger();
        startServer();
        users = new ArrayList<>();

        virtualView = new VirtualView();
        controller = new Controller(virtualView);
        virtualView.setServer(this);

        //Waits for the first player to connect
        while(users.size() < 1){
            System.out.println("Waiting for users to connect...");
            login();
        }
        controller.setUsers(users);

        //Insertion of Game Settings and Creation of the Game
        controller.gameCreation();

        while(users.size() < getGame().getNumPlayers()){
            System.out.println("Waiting for users to connect...");
            login();
        }
        controller.setUsers(users);

        //Starts the continuous check of connections with ping messages
        for(int i = 0 ; i < users.size(); i ++){
            virtualView.sendPing(users.get(i));
            virtualView.receivePing(users.get(i));

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

        new Server();

    }

    @Override
    public void run() {}
}
