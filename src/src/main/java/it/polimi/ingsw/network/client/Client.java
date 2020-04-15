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

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public abstract void startConnection(Socket clientConnection) throws IOException;

    public void sendMessage(Message message) throws IOException{}

    public abstract void close() throws Exception;


}
