package it.polimi.ingsw.server.model.exceptions;

public class InvalidConstructionException extends Exception {
    public InvalidConstructionException(){
        super("This worker doesn't belong to your player!");
    }
}
