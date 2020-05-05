package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.ConnectionRequest;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import static it.polimi.ingsw.network.JsonHelper.deserialization;


/**
 *
 * The SocketConnection class implements a socket that receives the message.
 *
 */

public class SocketConnection implements Runnable, ServerConnection {

    private final Socket socket;
    private final Object outLock = new Object();
    private final Object inLock = new Object();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean active;
    private SocketServer server;
    private String username;

    public SocketConnection(SocketServer socketServer, Socket socket){

        this.server = socketServer;
        this.socket = socket;
        this.active = true;

        try {
            synchronized (inLock) {
                this.in = new ObjectInputStream(socket.getInputStream());
            }

            synchronized (outLock) {
                this.out = new ObjectOutputStream(socket.getOutputStream());
            }
        } catch (IOException e) {
            Server.LOGGER.severe(e.toString());
        }

    }


    public SocketServer getServer(){
        return server;
    }


    /**
     *
     * run method continue to listen the input and send the messages to the server in case of message
     *
     */
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (inLock) {

                    Message m = (Message) in.readObject();
                    ConnectionRequest request = (ConnectionRequest) deserialization(m.getContent());
                    username = request.getSenderUsername();
                    System.out.println("Connection request received");

                    if (!server.getUsers().contains(username)) {
                        server.login(username, this);
                        Message logSuccessfull = new Message("You logged in successfully");
                        sendServerMessage(logSuccessfull);
                        System.out.println("Server sent Log response");
                        break;

                    } else {
                        Message logDenied = new Message("Your username is already taken");
                        sendServerMessage(logDenied);
                        break;

                    }
                }
            }catch(IOException e){
                disconnect();
            } catch(ClassNotFoundException e){
                Server.LOGGER.severe(e.getMessage());
            }
        }
    }

    /**
     *
     * The isActive method is used to check if the Socketconnection is active.
     *
     */

    @Override
    public boolean isActive() {
        return active && !socket.isClosed();
    }


    /**
     * The sendServerMessage method elaborates the server message
     *
     * @param serverMessage is the string message to be elaborated.
     */

    @Override
    public void sendServerMessage(Message serverMessage){
        try {
            out.writeObject(serverMessage);
            out.reset();
        } catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
            server.disconnect(username);
            disconnect();
        }
    }


    public Message receiveMessage() throws IOException, ClassNotFoundException {
        if (!Thread.currentThread().isInterrupted()) {
            synchronized (inLock) {
                Message m = (Message) in.readObject();
                return m;
            }
        }else {
            throw new IOException();
        }
    }


    @Override
    public void disconnect() {
        if (active) {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());
            }

            active = false;
        }

    }
}




