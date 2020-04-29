package it.polimi.ingsw.server;

import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.network.server.SocketServer;
import it.polimi.ingsw.network.server.VirtualView;
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


    private ArrayList<String> users;
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
    public static void login(String username, ServerConnection connection) {

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
        controller.setUsers(users);


        //    START OF THE COMMUNICATION WITH THE CLIENTS TO GET THE GAME'S SETTINGS

        //Insertion of Game Settings and Creation of the Game
        controller.gameCreation();

        game.gameUpdate();


        //Players enter the game until the game is full

        controller.playersEnter();
        game.gameUpdate();

        if (game.isWithGods()) {

            //Selection of God Cards
            controller.cardsSelection();


            //Each player chooses its card

            controller.godSelection();

            //First Player gets the remaining card
            String card = game.getGods().get(0);
            game.chooseCard(game.getPlayers().get(0), card);
            CardSelectionEvent event = new CardSelectionEvent(game.getIDs().get(0), card);
            game.notify(event);


            //Selection of the Starting Player

            controller.starterSelecion();
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
        controller.workersPlacement();


        //Starts the turns alternation between players

        controller.turns();


        String goodbye = "Game over, thanks for playing Santorini!";
        for (int i = 0; i < game.getIDs().size(); i++) {
            CommunicationEvent event = new CommunicationEvent(game.getIDs().get(i), goodbye);
            game.notify(event);
        }
    }


    public static void main(String[] args) throws InvalidInputException, WorkerNotExistException, IOException {

        new Server();

    }






}
