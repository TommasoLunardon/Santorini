package it.polimi.ingsw.server.model.exceptions;

/**
 * Exception thrown when Athena Condition isn't satisfied
 */
public class AthenaConditionException extends Exception {
    public AthenaConditionException(){
        super("You didn't respect the Athena Condition!");
    }
}
