package it.polimi.ingsw.network.events;

import it.polimi.ingsw.network.events.vcevents.*;

import java.io.IOException;


public interface VCEventSender extends EventSender {

     @Override
     void send(Event event);

     void send(ColorSelectedEvent event) throws IOException;

     void send(BoxSelectedEvent event) throws IOException;

     void send(CardSelectedEvent event) throws IOException;

     void send(GodsSelectedEvent event) throws IOException;

     void send(NumPlayersSelectedEvent event) throws IOException;

     void send(PlayerDataEnteredEvent event) throws IOException;

     void send(StarterSelectedEvent event) throws IOException;

     void send(UseofSpecialPowerEvent event) throws IOException;

     void send(WithGodsSelectedEvent event) throws IOException;

     void send(WorkerSelectedEvent event) throws IOException;


}
