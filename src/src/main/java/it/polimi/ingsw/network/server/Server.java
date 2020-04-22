package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Server implements Runnable {

    private final Object clientsLock = new Object();
    private static Map<String, ServerConnection> clients;
    private int socketPort;
    public static final Logger LOGGER = Logger.getLogger("Server");

    /**
     *
     * The Server method starts with a new game
     *
     */
    public Server() {
        initLogger();
        synchronized (clientsLock) {
            clients = new HashMap<>();
        }
        startServer();
    }

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
     *
     * Login method Adds player to the server
     *
     */
    static void login(String username, ServerConnection connection) {
        clients.put(username, connection);
        LOGGER.log(Level.INFO, "{0} connected to server!", username);
    }

    /**
     * The sendMessage method sends a message to client
     * @param username username of the client who will receive the message
     * @param message message to send
     */
    public void sendMessage(String username,Message message){
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

        LOGGER.log(Level.INFO, "Send: {0}, {1}", new Object[]{message.getSenderUsername(), message});

    }
    /**
     * The receivedMessage method process a message sent to server
     *
     */
    void receivedMessage(Message message) {

    }

    @Override
    public void run() {
    }


    public static void main(String[] args) {

    }
}
