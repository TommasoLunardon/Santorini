package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected workers aren't valid
 */
public class NotValidWorkersException extends Exception {
    public NotValidWorkersException(){
        super("These workers don't belong to the map");
    }
}
