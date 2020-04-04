package it.polimi.ingsw.server.model.exceptions;

public class NotValidPlayersException extends Exception {
    public NotValidPlayersException(){
        super("These players don't belong to the map");
    }
}
