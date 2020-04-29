package it.polimi.ingsw.network.events;

import java.io.IOException;

/**
 *  Interface representing the generic VCEvent sender.
 */


public interface VCEventSender extends EventSender {

     @Override
    void send(Event event);

     void send(VCEvent event) throws IOException;


}
