package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected worker doesn't exist
 */
public class WorkerNotExistException extends Exception {

    public WorkerNotExistException() {
        super("This Box doesn't have a Worker!");

    }
}
