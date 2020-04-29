package it.polimi.ingsw.network.events;

/**
 *  Interface representing the generic event sender, extended by VCEvent senders and MVEvent senders.
 */


public interface EventSender {

     void send(Event event);
}
