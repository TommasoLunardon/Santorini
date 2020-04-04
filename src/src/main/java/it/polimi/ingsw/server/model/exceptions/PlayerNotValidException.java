package it.polimi.ingsw.server.model.exceptions;

public class PlayerNotValidException extends Exception {
    public PlayerNotValidException(){
        super("This player doesn't belong to the game");
    }
}
