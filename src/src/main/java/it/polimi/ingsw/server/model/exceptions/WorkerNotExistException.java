package it.polimi.ingsw.server.model.exceptions;

public class WorkerNotExistException extends Exception {

    public WorkerNotExistException() {
        super("This Box doesn't have a Worker!");

    }
}
