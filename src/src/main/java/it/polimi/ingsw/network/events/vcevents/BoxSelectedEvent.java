package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.Box;

import java.io.IOException;


public class BoxSelectedEvent extends VCEvent {

    private Box box;

    public BoxSelectedEvent(String origin, Box box) {
        super(origin);
        this.box = box;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }

    public Box getBox() {
        Box b = box;
        return b;
    }
}