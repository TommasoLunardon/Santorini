package it.polimi.ingsw.network.events.vcevents;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.Worker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WorkerSelectedEvent extends VCEvent {

    private Worker worker;

    public WorkerSelectedEvent(String origin, Worker worker) {
        super(origin);
        this.worker = worker;
    }

    public String toString(){
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(this);
            so.flush();
            return Base64.encode(bo.toByteArray());
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
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

