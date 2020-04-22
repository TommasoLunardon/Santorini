package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;


/**
 *
 * The SocketConnection class implements a socket that receive the message.
 *
 */

public class SocketConnection implements Runnable, ServerConnection {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean active;
    private Thread listener;

    SocketConnection(SocketServer socketServer, Socket clientConnection) throws IOException {
        this.socket = clientConnection;
        this.active = true;

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        listener = new Thread(this);
        listener.start();
    }
    /**
     *
     * run method continue to listen the input and send the messages to the server in case of message
     *
     */
    @Override
    public void run() {
        while (isActive()) try {
            Message message = (Message) in.readObject();
            if (message != null) {
                if (message.getContent().equals("CONNECTION")) {
                    SocketServer.login(message.getSenderUsername(), this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getGlobal().warning(e.getMessage());
            disconnect();
        }
    }

    /**
     *
     * The isActive method is used to check if the Socketconnection is active.
     *
     */

    @Override
    public boolean isActive() {
        return active && !socket.isClosed();
    }


    /**
     * The sendServerMessage method elaborates the server message
     *
     * @param serverMessage is the string message to be elaborated.
     */

    @Override
    public void sendServerMessage(Message serverMessage){
        try {
            out.writeObject(serverMessage);
            out.reset();
        } catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
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

            listener.interrupt(); // Interrupts the thread
            active = false;
        }

    }
}




