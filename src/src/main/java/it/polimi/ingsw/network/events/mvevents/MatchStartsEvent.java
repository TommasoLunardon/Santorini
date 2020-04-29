package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;

public class MatchStartsEvent extends MVEvent {

    public MatchStartsEvent(String target) {
        super(target);
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);
    }
}
