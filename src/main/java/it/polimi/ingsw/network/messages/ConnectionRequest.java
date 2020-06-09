package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.events.Event;

/**
 * Message sent from the client to the server to request connection
 */
public class ConnectionRequest extends Event {

    private final String username;
    private final String content;

    public ConnectionRequest(String username) {
        this.content = "CONNECTION";
        this.username = username;
    }

    @Override
    public String toString() {
        return "ConnectionRequest{" +
                "senderUsername=" + getSenderUsername() +
                ", content=" + getContent() +
                "}";
    }

    public String getContent() {
        return content;
    }

    public String getSenderUsername() {
        return username;
    }
}
