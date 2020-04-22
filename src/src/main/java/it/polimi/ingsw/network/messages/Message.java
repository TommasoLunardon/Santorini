package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 *
 * Message object is the message sent from the client to the server.
 * It contains the player's username and the message.
 *
 * @author Jing Huang
 *
 */

public abstract class Message implements Serializable {
    private static final long serialVersionUID =1L ;


    private final String senderUsername;
    private final String messagecontent;

    /**
     *
     * Constructs a message with a player and a message.
     *
     * @param senderUsername the players's name.
     * @param messagecontent the message.
     *
     */

    Message(String senderUsername, String messagecontent) {
        this.senderUsername = senderUsername;
        this.messagecontent = messagecontent;
    }

    /**
     *
     * The getSenderUsername is used to obtain the player's name.
     *
     * @return the String of username.
     *
     */

    public String getSenderUsername() {
        return senderUsername;
    }

    /**
     *
     * The getContent is used to obtain the message.
     *
     * @return the String of message
     *
     */

    public String getContent() {
        return messagecontent;
    }


}


