package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;

import java.io.IOException;
/**
 * Event that communicates the selection of the game settings (number of players) by the client
 */
public class NumPlayersSelectedEvent extends VCEvent {

    private int num;

    public NumPlayersSelectedEvent(String origin, int number) {
        super(origin);
        this.num = number;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);
    }

    public int getNum() {
        return num;
    }
}
