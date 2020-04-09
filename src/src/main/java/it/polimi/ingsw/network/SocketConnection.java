package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.ClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketConnection implements it.polimi.ingsw.network.SocketController {

    private final Socket socket;
    private final SocketServer socketServer;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean active;
    private Thread listener;

    SocketConnection(SocketServer socketServer, Socket socket) throws IOException {
        this.socketServer = socketServer;
        this.socket = socket;
        this.active = true;

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        listener = new Thread((Runnable) this);
        listener.start();
    }
    @Override
    public void run() throws IOException, ClassNotFoundException {
        while (!Thread.currentThread().isInterrupted()) {
            ClientMessage clientMessage = (ClientMessage) in.readObject();
            while() {

            }

            }

    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void disconnect() {
    }


}

