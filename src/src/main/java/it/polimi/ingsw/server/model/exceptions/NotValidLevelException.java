package it.polimi.ingsw.server.model.exceptions;

public class NotValidLevelException extends Exception {
    public NotValidLevelException() {
        super("The level must be between 0 and 4");
    }
}
