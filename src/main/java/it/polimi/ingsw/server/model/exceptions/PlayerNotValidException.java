package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the selected player isn't valid
 */
public class PlayerNotValidException extends Exception {
    public PlayerNotValidException(){
        super("This player doesn't belong to the game");
    }
}
