package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected level isn't valid
 */
public class NotValidLevelException extends Exception {
    public NotValidLevelException() {
        super("The level must be between 0 and 4");
    }
}
