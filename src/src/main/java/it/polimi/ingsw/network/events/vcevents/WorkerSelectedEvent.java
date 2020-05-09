package it.polimi.ingsw.network.events.vcevents;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.Worker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WorkerSelectedEvent extends VCEvent {

    private int worker;

    public WorkerSelectedEvent(String origin, int worker) {
        super(origin);
        this.worker = worker;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }

    public int getWorker() {
        return worker;
    }
}

