package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected input isn't valid
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(){
        super("The value you provided can't be selected");
    }
}
