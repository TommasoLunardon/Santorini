package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;

public interface Connection {
    boolean isActive();

    void setActive(boolean active);

    void sendMessage(Message message) throws IOException;

    void disconnect();
}
