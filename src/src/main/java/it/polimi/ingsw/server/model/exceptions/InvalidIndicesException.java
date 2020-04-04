package it.polimi.ingsw.server.model.exceptions;

public class InvalidIndicesException extends Exception {
    public InvalidIndicesException(){
        super("This indices don't belong to the map");
    }
}
