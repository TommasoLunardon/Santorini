package it.polimi.ingsw.network.events;

import it.polimi.ingsw.network.events.vcevents.*;

import java.io.IOException;


public interface VCEventSender extends EventSender {

     @Override
    void send(Event event);

     void send(VCEvent event) throws IOException;


}
