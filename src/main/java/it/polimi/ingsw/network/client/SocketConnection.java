package it.polimi.ingsw.network.client;


import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.ConnectionRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

/**
 * Class used to implement the client's connection to the server
 */
public class SocketConnection extends ClientConnection implements Runnable {

    private transient java.net.Socket socket;
    private transient ObjectInputStream in;
    private transient ObjectOutputStream out;
    private Message message;
    private String host;

    public SocketConnection(String username, int port, String host) {
        super(username, port);
        this.host = host;
    }

    public Message getMessage(){return message;}

    /**
     *
     * The startConnection method obtains a connection with server
     *
     */
    @Override
    public void startConnection() throws IOException {
        socket = new Socket(host, getPort());
        socket.setSoTimeout(20000);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        String m = JsonHelper.serialization(new ConnectionRequest(getUsername()));
        Message message = new Message(m);

        sendClientMessage(message);
    }
    /**
     *
     * The sendClientMessage sends a message to server
     *
     */
    @Override
    public void sendClientMessage(Message message) throws IOException {
        if (out != null) {
            out.writeObject(message);
            out.reset();
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                message = (Message) in.readObject();
                break;
            } catch (IOException | ClassNotFoundException | NullPointerException e){
                System.out.println("Sorry but the connection went down and the game ended");
                disconnect();
                System.exit(0);
            }
        }
    }
    /**
     *
     * The disconnect method interrupts the connection
     *
     */
    private void disconnect() {
        try {
            System.out.println("You are being disconnected");
            close();
        } catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
        }
    }

    /**
     *
     * The close method closes the connection with server
     *
     */
    @Override
    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
        in = null;
        out = null;
        Thread.currentThread().interrupt();
    }


}