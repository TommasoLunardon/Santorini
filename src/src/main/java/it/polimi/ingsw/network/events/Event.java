package it.polimi.ingsw.network.events;

import it.polimi.ingsw.network.JsonHelper;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;

/**
 * Abstract class Event that provides the method manage(), used to manage the forwarding of events, it is extended by MVEvent and VCEvent.
 */



public abstract class Event implements Serializable {


    public String toString(){
        return JsonHelper.serialization(this);
    }

    public void manage(EventSender eventSender){
        eventSender.send(this);

    }

    public void manage(MVEventSender eventSender) throws SocketTimeoutException {
        eventSender.send(this);

    }

    public void manage(VCEventSender eventSender) throws IOException {

        eventSender.send(this);
    }


}
