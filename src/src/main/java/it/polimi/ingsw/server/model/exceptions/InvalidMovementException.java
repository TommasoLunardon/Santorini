package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the movement performed isn't valid
 */
public class InvalidMovementException extends Exception {
    public InvalidMovementException() {
        super("This worker doesn't belong to the player, therefore it can't be moved");
    }
}
