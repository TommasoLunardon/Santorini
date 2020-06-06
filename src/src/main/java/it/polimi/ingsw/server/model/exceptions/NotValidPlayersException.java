package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected players aren't valid
 */
public class NotValidPlayersException extends Exception {
    public NotValidPlayersException(){
        super("These players don't belong to the map");
    }
}
