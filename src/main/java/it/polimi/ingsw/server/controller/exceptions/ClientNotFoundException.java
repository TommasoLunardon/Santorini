package it.polimi.ingsw.server.controller.exceptions;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException(){super("Waiting for a message from the client");}

    @Override
    public String toString() {
        return  "Waiting for a message from the client";
    }
}
