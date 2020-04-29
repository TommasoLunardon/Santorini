package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;

public abstract class ServerConnection {
    private boolean active = true;
    /**
     * the isActive method return the connection status
     */
    public boolean isActive() {
        return active;
    }


    /**
     *
     * The sendServerMessage sends a message to the client
     *
     */

    public abstract void sendServerMessage(Message message) throws IOException;

    /**
     * The disconnect method disconnect from the client
     */
    public abstract void disconnect();
}
