package it.polimi.ingsw.server.controller.exceptions;

public class InvalidSenderException extends Exception {
    public InvalidSenderException(){super("This isn't the right sender");}

}
