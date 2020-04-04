package it.polimi.ingsw.server.model.exceptions;

public class NotValidWorkersException extends Exception {
    public NotValidWorkersException(){
        super("These workers don't belong to the map");
    }
}
