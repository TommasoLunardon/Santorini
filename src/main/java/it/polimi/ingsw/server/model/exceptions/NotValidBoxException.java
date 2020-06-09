package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected box isn't valid
 */
public class NotValidBoxException extends Exception {
    public NotValidBoxException(){
        super("Parameters not valid for the box creation");

    }
}

