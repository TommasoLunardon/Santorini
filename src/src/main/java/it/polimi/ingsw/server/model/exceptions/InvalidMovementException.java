package it.polimi.ingsw.server.model.exceptions;

public class InvalidMovementException extends Exception {
    public InvalidMovementException() {
        super("This worker doesn't belong to the player, therefore it can't be moved");
    }
}
