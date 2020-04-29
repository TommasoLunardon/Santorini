package it.polimi.ingsw.network.events;

import it.polimi.ingsw.network.events.mvevents.*;

public interface MVEventSender extends  EventSender {

    @Override
     void send(Event event);

     void send(MVEvent event);

}
