package it.polimi.ingsw.network;

import java.io.IOException;

public interface SocketController {

    void run() throws IOException, ClassNotFoundException;

    boolean isActive();

    void setActive(boolean active);

    void disconnect();
}
