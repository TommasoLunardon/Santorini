package it.polimi.ingsw.network.client;


import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.ConnectionRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;


public class SocketConnection extends Client implements Runnable {

    private transient java.net.Socket socket;
    private transient ObjectInputStream in;
    private transient ObjectOutputStream out;
    private transient Thread messageReceiver;
    private Message message;
    public final JsonHelper helper = new JsonHelper();

    public SocketConnection(String username, int port) {
        super(username, port);
    }

    public Message getMessage(){return message;}

    /**
     *
     * The startConnection method obtain a connection with server
     *
     */
    @Override
    public void startConnection() throws IOException {
        socket = new java.net.Socket(getUsername(), getPort());
        out = new ObjectOutputStream(socket.getOutputStream());
        socket.setSoTimeout(20000);
        in = new ObjectInputStream(socket.getInputStream());

        String m = helper.serialization(new ConnectionRequest(getUsername()));
        Message message = new Message(m);

        sendClientMessage(message);

        messageReceiver = new Thread(this);
        messageReceiver.start();

    }
    /**
     *
     * The sendClientMessage send message to server
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
            } catch (IOException | ClassNotFoundException e) {
                Logger.getGlobal().warning(e.getMessage());
                disconnect();
            }

        }
    }
    /**
     *
     * The disconnect method interrupts the thread
     *
     */
    private void disconnect() {
        try {
            close();
        } catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
        }
    }

    /**
     *
     * The close method close the connection with server
     *
     */
    @Override
    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
        messageReceiver.interrupt();

        in = null;
        out = null;
    }


}