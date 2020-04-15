package it.polimi.ingsw.network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static final long serialVersionUID =1L ;


    private final String senderUsername;
    private final Content messagecontent;

    Message(String senderUsername, Content messagecontent) {
        this.senderUsername = senderUsername;
        this.messagecontent = messagecontent;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public Content getContent() {
        return messagecontent;
    }


}


