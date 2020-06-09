package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the construction performed isn't valid
 */
public class WrongConstructionException extends Exception {
    public WrongConstructionException(){
        super("This construction cannot be performed!");
    }
}
