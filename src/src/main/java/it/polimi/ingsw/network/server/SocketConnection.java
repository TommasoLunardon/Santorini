package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Content;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;


public class SocketConnection implements Runnable, Connection {

    private final Socket socket;
    private final SocketServer socketServer;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean active;
    private Thread listener;
    private Game game;

    SocketConnection(SocketServer socketServer, Socket socket) throws IOException {
        this.socketServer = socketServer;
        this.socket = socket;
        this.active = true;
        this.game = game;

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        listener = new Thread((Runnable) this);
        listener.start();
    }

    @Override
    public void run() {
        while (isActive()) try {
            Message message = (Message) in.readObject();
            if (message != null) {
                if (message.getContent() == Content.CONNECTION) {
                    socketServer.login(message.getSenderUsername(), this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getGlobal().warning(e.getMessage());
            active = false;
            disconnect();
        }
    }

    @Override
    public boolean isActive() {
        return active && !socket.isClosed();
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        if (active)
            try {
                out.writeObject(message);
                out.reset();
            } catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());
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

            listener.interrupt(); // Interrupts the thread
            active = false;
        }
    }
}




