package it.polimi.ingsw.server.model.exceptions;

public class WrongPlayers_WorkersException extends Exception {
    public WrongPlayers_WorkersException(){
        super("The workers-players assignment is not correct");
    }
}
