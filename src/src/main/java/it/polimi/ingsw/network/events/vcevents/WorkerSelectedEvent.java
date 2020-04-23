package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.Worker;

import java.io.IOException;

public class WorkerSelectedEvent extends VCEvent {

    private Worker worker;

    public WorkerSelectedEvent(String origin, Worker worker) {
        super(origin);
        this.worker = worker;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }

    public Worker getWorker() {
        Worker w = worker;
        return w;
    }
}

