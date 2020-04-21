package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;

public class LoserPlayerEvent extends MVEvent {

    public LoserPlayerEvent(String target) {
        super(target);
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);

    }
}
