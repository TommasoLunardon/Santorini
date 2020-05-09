package it.polimi.ingsw.server;

import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.SocketServer;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exceptions.*;


import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Class Server used to perform an entire game of Santorini with clients connected to the server
 *  Needs to be completed with the management of "PING" Messages
 */

public class Server implements Runnable, Serializable {

    private final Object clientsLock = new Object();
    private static SocketServer socketServer;
    private static final int socketPort = 65535;
    public static final Logger LOGGER = Logger.getLogger("Server");

    private ArrayList<String> users;
    private static Controller controller;
    private static VirtualView virtualView;

    //Game used for multiple matches
    private static Game baseGame;

    private void startServer() {
        try {
            socketServer = new SocketServer(socketPort);
            socketServer.start();
            LOGGER.info("Socket Server Started");
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

    }

    private void disconnect(String user){
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
    private synchronized void login() {
        socketServer.run();
        users = socketServer.getUsers();
    }

    /**
     * The sendMessage method sends a message to client
     * @param username username of the client who will receive the message
     * @param message  message to send
     */
    public void sendMessage(String username, Message message) throws SocketTimeoutException {
        synchronized (clientsLock) {
            if(users.contains(username)) {
                socketServer.sendMessage(username, message);
            }else{
                throw new SocketTimeoutException();
            }
        }
        LOGGER.log(Level.INFO, "Send: {0}, {1}", new Object[]{username, message.getContent()});
    }


    /**
     * The receive method returns the message sent to the server by a specific user
     */
    public String receive(String ID) {
        return socketServer.receiveMessage(ID).getContent();

    }

    public static Game getGame() {
        return controller.getGame();
    }

    /**
     * The Server method starts with a new game
     */
    private Server() throws InvalidInputException, WorkerNotExistException{

        boolean restart = true;
        startServer();
        users = new ArrayList<>();

        virtualView = new VirtualView();
        controller = new Controller(virtualView);
        virtualView.setServer(this);

        //Waits for the first player to connect
        System.out.println("Waiting for users to connect...");
        while(users.size() < 1){
            try{
                login();
            }catch (NullPointerException e){
                System.out.println("Login went wrong");
            }
        }
        controller.setUsers(users);

        virtualView.sendPing(users.get(0));

        //Insertion of Game Settings and Creation of the Game
        try {
            controller.gameCreation();
        } catch (SocketTimeoutException e) {
            System.out.println("Connection ended");
            System.exit(0);
        }

        System.out.println("Waiting for users to connect...");
        while(users.size() < getGame().getNumPlayers()){
            login();
        }
        controller.setUsers(users);

        //Starts the continuous check of connections with ping messages
        for (String user : users) {
            if(!user.equals(users.get(0))){
                virtualView.sendPing(user);
            }

        }
        //Players enter the game until the game is full
        try {
            controller.playersEnter();
        } catch (SocketTimeoutException e) {
            System.out.println("Connection ended");
            System.exit(0);
        }

        //Base game for restarting the match
        baseGame =  getGame();

        //Starting Point for multiple matches with the same settings
        while(restart) {
            controller.setGame(baseGame);

            if (getGame().isWithGods()) {

                //Selection of God Cards
                try {
                    controller.cardsSelection();
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }

                //Each player chooses its card
                try {
                    controller.godSelection();
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }

                //First Player gets the remaining card
                String card = getGame().getGods().get(0);
                getGame().chooseCard(getGame().getPlayers().get(0), card);
                CardSelectionEvent event = new CardSelectionEvent(getGame().getIDs().get(0), card);
                try {
                    virtualView.send(event);
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }

                //Selection of the Starting Player
                try {
                    controller.starterSelection();
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }
            }

            //Game without gods ----> starts the youngest player
            if (!getGame().isWithGods()) {
                int min = getGame().getMinAge();
                for (int i = 0; i < getGame().getNumPlayers(); i++) {
                    if (getGame().getPlayers().get(i).getPlayerAge() == min) {
                        getGame().chooseStarter(getGame().getPlayers().get(i));
                        StarterSelectionEvent event = new StarterSelectionEvent(getGame().getIDs().get(0), getGame());
                        try {
                            virtualView.send(event);
                        } catch (SocketTimeoutException e) {
                            System.out.println("Connection ended");
                            System.exit(0);
                        }
                        break;
                    }
                }
            }

            //Placement of Workers//
            try {
                controller.workersPlacement();
            } catch (SocketTimeoutException e) {
                System.out.println("Connection ended");
                System.exit(0);
            }

            //Starts the turns alternation between players
            try {
                controller.turns();
            } catch (SocketTimeoutException e) {
                System.out.println("Connection ended");
                System.exit(0);
            }

            String gameover = "Game over, thanks for playing Santorini!";
            for (int i = 0; i < getGame().getIDs().size(); i++) {
                CommunicationEvent event = new CommunicationEvent(getGame().getIDs().get(i), gameover);
                try {
                    virtualView.send(event);
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }
            }

            //Ask for another match
            boolean received = false;
            while (!received) {
                try {
                    controller.setActive(users.get(0));
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }
                String restartSelection = "Do you want to restart?";
                CommunicationEvent event = new CommunicationEvent(users.get(0), restartSelection);
                try {
                    virtualView.send(event);
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }

                try {
                    restart = virtualView.receiveRestartConfirmationEvent(users.get(0));
                    received = true;
                } catch (InvalidSenderException e) {
                    InvalidInputEvent ev = new InvalidInputEvent(users.get(0));
                    try {
                        virtualView.send(ev);
                    } catch (SocketTimeoutException ex) {
                        System.out.println("Connection ended");
                        System.exit(0);
                    }
                }
            }
        }
        //End of the Game all clients are disconnected
        String goodbye = "The host decided to conclude the matches";
        for (int i = 0; i < getGame().getIDs().size(); i++) {
            CommunicationEvent event = new CommunicationEvent(getGame().getIDs().get(i), goodbye);
            try {
                virtualView.send(event);
            } catch (SocketTimeoutException e) {
                System.out.println("Connection ended");
                break;
            }
            disconnect(getGame().getIDs().get(i));
        }
    }

    public static void main(String[] args) throws InvalidInputException, WorkerNotExistException{

        new Server();
    }

    @Override
    public void run() {}
}
