package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the worker-player assignment isn't valid
 */
public class WrongPlayers_WorkersException extends Exception {
    public WrongPlayers_WorkersException(){
        super("The workers-players assignment is not correct");
    }
}
