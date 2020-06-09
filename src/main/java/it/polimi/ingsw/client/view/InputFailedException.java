package it.polimi.ingsw.client.view;

/**
 * Exception thrown when some methods are used with an invalid input
 */

public class InputFailedException extends Exception{
    public InputFailedException(){
        super(("Input Failure"));
    }
}