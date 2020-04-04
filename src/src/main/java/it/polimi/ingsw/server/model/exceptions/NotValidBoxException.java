package it.polimi.ingsw.server.model.exceptions;

public class NotValidBoxException extends Exception {
    public NotValidBoxException(){
        super("Parameters not valid for the box creation");

    }
}

