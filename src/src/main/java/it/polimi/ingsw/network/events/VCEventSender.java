package it.polimi.ingsw.network.events;

import it.polimi.ingsw.network.events.vcevents.*;


 public interface VCEventSender extends EventSender {

     @Override
    void send(Event event);

     void send(BoxSelectedEvent event);

     void send(CardSelectedEvent event);

     void send(GodsSelectedEvent event);

     void send(NumPlayersSelectedEvent event);

     void send(PlayerDataEnteredEvent event);

     void send(StarterSelectedEvent event);

     void send(UseofSpecialPowerEvent event);

     void send(WithGodsSelectedEvent event);

     void send(WorkerSelectedEvent event);


}
