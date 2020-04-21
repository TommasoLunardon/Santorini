package it.polimi.ingsw.network.events;

public abstract class MVEvent extends Event {
    private String target;

    public MVEvent(String target){
        this.target = target;
    }

    public abstract void manage(MVEventSender eventSender);


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
