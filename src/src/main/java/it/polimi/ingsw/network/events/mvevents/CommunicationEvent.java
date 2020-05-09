package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;

import java.net.SocketTimeoutException;

public class CommunicationEvent extends MVEvent {
    private String message;

    public CommunicationEvent(String target, String message) {
        super(target);
        this.message = message;
    }

    @Override
    public void manage(MVEventSender eventSender) throws SocketTimeoutException {
        eventSender.send(this);


    }

  public String getMessage(){
        return message;
  }
}
