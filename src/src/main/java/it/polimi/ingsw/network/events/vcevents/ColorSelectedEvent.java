package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.PlayerColor;

import java.io.IOException;

public class ColorSelectedEvent extends VCEvent {

    private PlayerColor color;

    public ColorSelectedEvent(String origin, PlayerColor color) {
        super(origin);
        this.color = color;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }

    public PlayerColor getColor() {
        PlayerColor c = color;
        return c;
    }
}
