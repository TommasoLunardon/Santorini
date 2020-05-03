package it.polimi.ingsw.client.view;

/**
 * these exceptions could be used for signal a failed input
 * @author Gabriele Gatti
 */

public class InputFailedException extends Exception{
    public InputFailedException(){
        super(("Input Failure"));
    }
}