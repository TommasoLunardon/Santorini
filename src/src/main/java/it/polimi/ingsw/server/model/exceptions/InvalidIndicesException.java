package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected indices aren't valid
 */
public class InvalidIndicesException extends Exception {
    public InvalidIndicesException(){
        super("This indices don't belong to the map");
    }
}
