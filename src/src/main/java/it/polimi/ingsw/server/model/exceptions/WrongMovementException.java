package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the movement performed isn't valid
 */
public class WrongMovementException extends Exception {
    public WrongMovementException(){
        super("This movement cannot be performed!");
    }
}
