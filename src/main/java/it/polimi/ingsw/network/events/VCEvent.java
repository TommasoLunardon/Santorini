package it.polimi.ingsw.network.events;

import java.io.IOException;

/**
 * Abstract Class VCEvent that represents the generic event sent from the client to the server
 */


public abstract class VCEvent extends Event {

    private String origin;

    public VCEvent(String origin){
        this.origin = origin;
    }

    /**
     *Method used to manage the forwarding of the event
     * @param eventSender class that performs the event forwarding
     */
    public abstract void manage(VCEventSender eventSender) throws IOException;


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
