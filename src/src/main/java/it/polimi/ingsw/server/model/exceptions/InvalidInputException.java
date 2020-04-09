package it.polimi.ingsw.server.model.exceptions;

public class InvalidInputException extends Exception {

    public InvalidInputException(){
        super("The value you provided can't be selected");
    }
}
