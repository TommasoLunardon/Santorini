package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 *
 * Class used to represent a generic message sent throughout our network
 */

public class Message implements Serializable {
    private static final long serialVersionUID =1L ;

    private final String messageContent;

    /**
     *
     * Constructs a message
     *
     * @param messageContent the message.
     *
     */

    public Message(String messageContent) {

        this.messageContent = messageContent;
    }

    /**
     *
     * Method used to obtain the message content.
     *
     * @return the message
     *
     */

    public String getContent() {
        return messageContent;
    }


}


