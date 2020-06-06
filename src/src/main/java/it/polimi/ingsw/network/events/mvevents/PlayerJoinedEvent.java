package it.polimi.ingsw.network.events.mvevents;

import java.util.Base64;
import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Player;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.SocketTimeoutException;
/**
 * Event that communicates to the client its data when he/her joins the game
 */
public class PlayerJoinedEvent extends MVEvent implements Serializable {

    private Player player;

    public PlayerJoinedEvent(String target, Player player) {
        super(target);
        this.player = player;
    }

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

    @Override
    public void manage(MVEventSender eventSender) throws SocketTimeoutException {
        eventSender.send(this);
    }

    public Player getP() {
        Player p = player;
        return p;
    }
}
