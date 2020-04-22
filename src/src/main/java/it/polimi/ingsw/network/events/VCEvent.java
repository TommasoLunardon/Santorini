package it.polimi.ingsw.network.events;

public abstract class VCEvent extends Event {

    private String origin;

    public VCEvent(String origin){
        this.origin = origin;
    }

    public abstract void manage(VCEventSender eventSender);


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
