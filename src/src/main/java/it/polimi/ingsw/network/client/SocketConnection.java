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


public class SocketConnection extends ClientConnection implements Runnable {

    private transient java.net.Socket socket;
    private transient ObjectInputStream in;
    private transient ObjectOutputStream out;
    private Message message;

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
        String host = InetAddress.getLocalHost().getHostName();
        socket = new Socket(host, getPort());
        //socket.setSoTimeout(20000);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        String m = JsonHelper.serialization(new ConnectionRequest(getUsername()));
        Message message = new Message(m);

        sendClientMessage(message);

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
        if (!Thread.currentThread().isInterrupted()) {
            try {
                message = (Message) in.readObject();
            } catch (SocketTimeoutException e){
                System.out.println("Sorry but the connection went down and the game ended");
                disconnect();
            } catch (IOException | ClassNotFoundException e) {
                 System.out.println("Exception during Sock. Con. run()");
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
    public void disconnect() {
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
        in = null;
        out = null;
    }


}