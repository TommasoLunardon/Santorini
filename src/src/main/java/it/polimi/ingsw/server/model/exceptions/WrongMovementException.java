package it.polimi.ingsw.server.model.exceptions;

public class WrongMovementException extends Exception {
    public WrongMovementException(){
        super("This movement cannot be performed!");
    }
}
