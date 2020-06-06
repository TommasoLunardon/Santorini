package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when construction performed isn't valid
 */
public class InvalidConstructionException extends Exception {
    public InvalidConstructionException(){
        super("This worker doesn't belong to your player!");
    }
}
