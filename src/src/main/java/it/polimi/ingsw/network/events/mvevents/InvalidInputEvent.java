package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;

public class InvalidInputEvent extends MVEvent {

    private Exception exc;

    public InvalidInputEvent(String target, Exception e) {
        super(target);
        this.exc = e;
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);

    }

    public Exception getException() {
        return exc;
    }
}
