package it.polimi.ingsw.network.events.vcevents;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.Box;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


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