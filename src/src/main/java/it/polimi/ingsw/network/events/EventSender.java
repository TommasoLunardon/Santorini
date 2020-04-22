package it.polimi.ingsw.network.events;

/*
   Interface representing the generic event sender, extended by VCEvent senders and MVEvent senders.
 */


/*
    Interface EventSender, it contains the method send(), the class is extended by MVEventSender and VCEventSender.

 */

public interface EventSender {

     void send(Event event);
}
