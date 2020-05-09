package it.polimi.ingsw.network.events;

import java.net.SocketTimeoutException;

/**
 * Abstract Class MVEvent that represents the generic event sent from the server to the client
 */

public abstract class MVEvent extends Event {
    private String target;

    public MVEvent(String target){
        this.target = target;
    }

    /**
     *  Manage the forwarding of the event
     * @param eventSender class that performs the event forwarding
     */
    public abstract void manage(MVEventSender eventSender) throws SocketTimeoutException;


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
