package it.polimi.ingsw.network.events;

import java.io.IOException;

public abstract class VCEvent extends Event {

    private String origin;

    public VCEvent(String origin){
        this.origin = origin;
    }

    public abstract void manage(VCEventSender eventSender) throws IOException;


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
