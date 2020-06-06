package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;

/**
 * Interface that represents the server to be implemented in our network
 */
public interface ServerConnection {

    /**
     * the isActive method return the connection status
     */
    boolean isActive();


    /**
     *
     * The sendServerMessage sends a message to the client
     *
     */

    void sendServerMessage(Message message) throws IOException;

    /**
     * The disconnect method disconnect from the client
     */
    void disconnect();
}
