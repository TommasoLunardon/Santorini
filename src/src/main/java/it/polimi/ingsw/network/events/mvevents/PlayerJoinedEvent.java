package it.polimi.ingsw.network.events.mvevents;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class PlayerJoinedEvent extends MVEvent {

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
            return Base64.encode(bo.toByteArray());
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);
    }

    public Player getP() {
        Player p = player;
        return p;
    }
}
