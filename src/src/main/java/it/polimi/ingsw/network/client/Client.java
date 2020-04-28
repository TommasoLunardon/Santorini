package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.Socket;

public abstract class Client {
    private final String username;
    private final int port;

    public Client(String username, int port) {
        this.username = username;
        this.port = port;
    }

    /**
     *
     * @return the username of the player
     *
     */

    public String getUsername() {
        return username;
    }

    /**
     *
     * @return the port of the server
     *
     */

    public int getPort() {
        return port;
    }

    /**
     *
     * The startConnection method obtain the connection with the server
     *
     */

    public abstract void startConnection(Socket clientConnection) throws IOException;

    /**
     * The sendClientMessage method Send a message to the server
     * @param message message to send to the server
     *
     */
    public void sendClientMessage(Message message) throws IOException{}

    /**
     *
     * The close method close the connection with the server
     *
     */

    public abstract void close() throws Exception;


}
