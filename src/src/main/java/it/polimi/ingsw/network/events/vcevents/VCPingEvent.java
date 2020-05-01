package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;

public class VCPingEvent extends VCEvent {

    public VCPingEvent(String origin) {
        super(origin);
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {

    }
}
