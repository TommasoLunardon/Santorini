package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;

public class UseofSpecialPowerEvent  extends VCEvent {

    private boolean condition;

    public UseofSpecialPowerEvent(String origin, boolean condition) {
        super(origin);
        this.condition = condition;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }

    public boolean getCondition(){
        return condition;
    }
}
