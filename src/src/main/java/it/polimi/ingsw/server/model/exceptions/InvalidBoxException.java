package it.polimi.ingsw.server.model.exceptions;

public class InvalidBoxException extends Exception {
    public InvalidBoxException(){
        super("This worker doesn't belong to the Map!");
    }
}
