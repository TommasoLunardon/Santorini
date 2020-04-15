package it.polimi.ingsw.network.client;


import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;


public class SocketClient extends Client implements Runnable {

    private transient Socket socket;
    private transient ObjectInputStream in;
    private transient ObjectOutputStream out;
    private Message Message;

    public SocketClient(String username, int port) {
        super(username, port);
    }

    @Override
    public void startConnection(Socket clientConnection) throws IOException {
        out = new ObjectOutputStream(clientConnection.getOutputStream());
        in = new ObjectInputStream(clientConnection.getInputStream());

        sendMessage(new Request(getUsername()));
        Thread messageReceiver = new Thread(this);
        messageReceiver.start();

    }

    @Override
    public void sendMessage(Message message) throws IOException {
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
            }

        }
    }
    @Override
    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
        in = null;
        out = null;
    }
}