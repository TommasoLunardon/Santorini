package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;
import java.util.ArrayList;

public class GodsSelectedEvent extends VCEvent {
    private ArrayList<String> gods;

    public GodsSelectedEvent(String origin, ArrayList<String> gods) {
        super(origin);
        this.gods = gods;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);
    }

    public ArrayList<String> getGods() {
        ArrayList<String> g = gods;
        return g;
    }
}
