package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;

import java.net.SocketTimeoutException;
/**
 * Event that communicates to the client that its construction wasn't valid
 */
public class InvalidConstructionEvent extends MVEvent {

    public InvalidConstructionEvent(String target) {
        super(target);
    }

    @Override
    public void manage(MVEventSender eventSender) throws SocketTimeoutException {
        eventSender.send(this);

    }
}