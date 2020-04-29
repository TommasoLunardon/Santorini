package it.polimi.ingsw.network.events;

import it.polimi.ingsw.network.events.mvevents.*;

public interface MVEventSender extends  EventSender {

    @Override
     void send(Event event);

     void send(CardSelectionEvent event);

     void send(CommunicationEvent event);

     void send(GameCreatedEvent event);

     void send(GameUpdatingEvent event);

     void send(GodCardsSelectedEvent event);

     void send(InvalidConstructionEvent event);

     void send(InvalidInputEvent event);

     void send(InvalidMovementEvent event);

     void send(LoserPlayerEvent event);

     void send(MatchStartsEvent event);

     void send(PlayerJoinedEvent event);

     void send(StarterSelectionEvent event);

     void send(WinnerPlayerEvent event);


}
