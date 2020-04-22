package it.polimi.ingsw.network.messages;

public class ConnectionRequest extends Message {
    public ConnectionRequest(String username) {
        super(username,"CONNECTION");
    }

    @Override
    public String toString() {
        return "ConnectionRequest{" +
                "senderUsername=" + getSenderUsername() +
                ", content=" + getContent() +
                "}";
    }

}
