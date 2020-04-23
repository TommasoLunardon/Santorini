package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.Box;


public class BoxSelectedEvent extends VCEvent {

    private Box box;

    public BoxSelectedEvent(String origin, Box box) {
        super(origin);
        this.box = box;
    }

    @Override
    public void manage(VCEventSender eventSender) {
        eventSender.send(this);

    }

    public Box getBox() {
        Box b = box;
        return b;
    }
}