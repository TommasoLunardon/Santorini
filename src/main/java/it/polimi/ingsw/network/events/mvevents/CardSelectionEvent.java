package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
/**
 * Event that confirms to the client which card he chose
 */
public class CardSelectionEvent extends MVEvent {

    private String selectedGod;

    public CardSelectionEvent(String target, String god) {
        super(target);
        selectedGod = god;
    }

    @Override
    public void manage(MVEventSender eventSender) throws SocketTimeoutException {
        eventSender.send(this);

    }

    public String getSelectedGod() {
        return selectedGod;
    }
}
