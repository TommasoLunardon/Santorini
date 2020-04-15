package it.polimi.ingsw.network.messages;

public class Request extends Message {


    public Request(String Username) {
        super(Username,Content.CONNECTION);
    }

    @Override
    public String toString() {
        return "ConnectionRequest{" +
                "Username=" + getSenderUsername() +
                ", content=" + getContent() +
                "}";
    }
}



