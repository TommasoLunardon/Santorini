package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

public class StarterSelectedEvent extends VCEvent {

    private String starter;

    public StarterSelectedEvent(String origin, String starter) {
        super(origin);
        this.starter = starter;
    }

    @Override
    public void manage(VCEventSender eventSender) {
        eventSender.send(this);

    }

    public String getStarter() {
        return starter;
    }
}
