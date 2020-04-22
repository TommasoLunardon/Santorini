package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

public class NumPlayersSelectedEvent extends VCEvent {

    private int num;

    public NumPlayersSelectedEvent(String origin, int number) {
        super(origin);
        this.num = number;
    }

    @Override
    public void manage(VCEventSender eventSender) {
        eventSender.send(this);
    }

    public int getNum() {
        return num;
    }
}
