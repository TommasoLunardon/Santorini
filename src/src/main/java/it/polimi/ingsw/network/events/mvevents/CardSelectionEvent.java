package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;

public class CardSelectionEvent extends MVEvent {

    private String selectedGod;

    public CardSelectionEvent(String target, String god) {
        super(target);
        selectedGod = god;
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);

    }

    public String getSelectedGod() {
        return selectedGod;
    }
}
