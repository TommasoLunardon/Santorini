package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 *
 * Message object is the message sent from the client to the server.
 *
 *
 * @author Jing Huang
 *
 */

public class Message implements Serializable {
    private static final long serialVersionUID =1L ;

    private final String messageContent;

    /**
     *
     * Constructs a message with a player and a message.
     *
     * @param messageContent the message.
     *
     */

    public Message(String messageContent) {

        this.messageContent = messageContent;
    }

    /**
     *
     * The getContent is used to obtain the message.
     *
     * @return the String of message
     *
     */

    public String getContent() {
        return messageContent;
    }


}


