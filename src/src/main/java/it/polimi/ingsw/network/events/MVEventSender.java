package it.polimi.ingsw.network.events;

/**
 *  Interface representing the generic MVEvent sender.
 */

public interface MVEventSender extends  EventSender {

    @Override
     void send(Event event);

     void send(MVEvent event);

}
