package it.polimi.ingsw.server.model.exceptions;

public class AthenaConditionException extends Exception {
    public AthenaConditionException(){
        super("You didn't respect the Athena Condition!");
    }
}
