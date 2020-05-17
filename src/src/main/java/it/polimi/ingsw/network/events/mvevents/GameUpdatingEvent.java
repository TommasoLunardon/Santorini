package it.polimi.ingsw.network.events.mvevents;

import java.util.Base64;
import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;

import java.io.*;
import java.net.SocketTimeoutException;

public class GameUpdatingEvent extends MVEvent implements Serializable {

    public String toString(){
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(this);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    private Game game;

    public GameUpdatingEvent(String target, Game game) {
        super(target);
        this.game = game;
    }

    @Override
    public void manage(MVEventSender eventSender) throws SocketTimeoutException {
        eventSender.send(this);

    }

    public Game getGame() {
        return game;
    }


}
