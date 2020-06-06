package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;

/**
 * Event that communicates the selection of a worker by the client
 */
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

