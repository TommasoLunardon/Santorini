package it.polimi.ingsw.network.client;


import it.polimi.ingsw.network.messages.ConnectionRequest;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;


public class SocketConnection extends Client implements Runnable {

    private transient java.net.Socket socket;
    private transient ObjectInputStream in;
    private transient ObjectOutputStream out;
    private transient Thread messageReceiver;
    private Message Message;

    public SocketConnection(String username, int port) {
        super(username, port);
    }

    /**
     *
     * The startConnection method obtain a connection with server
     *
     */
    @Override
    public void startConnection(java.net.Socket clientConnection) throws IOException {
        socket = new java.net.Socket(getUsername(), getPort());
        out = new ObjectOutputStream(clientConnection.getOutputStream());
        in = new ObjectInputStream(clientConnection.getInputStream());

        sendClientMessage(new ConnectionRequest(getUsername()));

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
                Message = (Message) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Logger.getGlobal().warning(e.getMessage());
                disconnect();
            }

        }
    }
    public ObjectOutputStream getOut() {
        return out;
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