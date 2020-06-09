package it.polimi.ingsw.network.server;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.events.vcevents.VCPingEvent;
import it.polimi.ingsw.network.messages.ConnectionRequest;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import static it.polimi.ingsw.network.JsonHelper.deserialization;

public class SocketConnection implements Runnable, ServerConnection {

    private final Socket socket;
    private final Object outLock = new Object();
    private final Object inLock = new Object();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean active;
    private SocketServer server;
    private String username;
    private Message lastReceived;

    SocketConnection(SocketServer socketServer, Socket socket){

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

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (inLock) {
                try {
                    lastReceived = (Message) in.readObject();

                    try {
                        ConnectionRequest request = (ConnectionRequest) deserialization(lastReceived.getContent());
                        if (request != null) {
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
                    }catch(ClassCastException | JsonSyntaxException e){
                        break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    disconnect();
                }
            }
        }
    }


    public Message getMessage(){
        return lastReceived;
    }

    @Override
    public boolean isActive() {
        return active && !socket.isClosed();
    }


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




