package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

public class CardSelectedEvent extends VCEvent {

    private String card;

    public CardSelectedEvent(String origin, String card) {
        super(origin);
        this.card = card;
    }

    @Override
    public void manage(VCEventSender eventSender) {
        eventSender.send(this);

    }

    public String getCard() {
        return card;
    }
}
