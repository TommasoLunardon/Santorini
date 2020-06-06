package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected box isn't valid
 */
public class InvalidBoxException extends Exception {
    public InvalidBoxException(){
        super("This worker doesn't belong to the Map!");
    }
}
