package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;

import java.net.SocketTimeoutException;
/**
 * Event that communicates to the client that its input wasn't valid
 */
public class InvalidInputEvent extends MVEvent {

    public InvalidInputEvent(String target) {
        super(target);
    }

    @Override
    public void manage(MVEventSender eventSender) throws SocketTimeoutException {
        eventSender.send(this);

    }
}

