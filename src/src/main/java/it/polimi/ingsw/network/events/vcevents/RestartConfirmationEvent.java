package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;
/**
 * Event that communicates the decision of restarting selected by the client
 */
public class RestartConfirmationEvent extends VCEvent {
    private boolean condition;


    public RestartConfirmationEvent(String origin, boolean condition) {
        super(origin);
        this.condition = condition;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {

    }

    public boolean getCondition() {
        return condition;
    }


}
