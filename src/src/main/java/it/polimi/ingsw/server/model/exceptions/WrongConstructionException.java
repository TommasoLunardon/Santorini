package it.polimi.ingsw.server.model.exceptions;

public class WrongConstructionException extends Exception {
    public WrongConstructionException(){
        super("This construction cannot be performed!");
    }
}
