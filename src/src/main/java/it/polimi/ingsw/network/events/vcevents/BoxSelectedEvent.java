package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;


/**
 * Event that communicates the selection of a box by the client
 */
public class BoxSelectedEvent extends VCEvent {

    private int[] box = new int[2];

    public BoxSelectedEvent(String origin, int x, int y) {
        super(origin);
        box[0] = x;
        box[1] = y;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }

    public int[] getBox() {
        int[] res = new int[2];
        res[0] = box[0];
        res[1] = box[1];
        return res;
    }
}