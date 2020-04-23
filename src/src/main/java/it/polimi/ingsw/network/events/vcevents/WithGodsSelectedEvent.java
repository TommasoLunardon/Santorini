package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;

public class WithGodsSelectedEvent extends VCEvent {

    private boolean condition;

    public WithGodsSelectedEvent(String origin, boolean con) {
        super(origin);
        this.condition = con;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);
    }

    public boolean getCondition() {
        return condition;
    }
}
