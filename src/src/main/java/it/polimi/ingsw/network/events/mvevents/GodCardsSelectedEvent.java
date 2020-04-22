package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;

public class GodCardsSelectedEvent extends MVEvent {

    private ArrayList<String> selectedCards;

    public GodCardsSelectedEvent(String target, Game game) {
        super(target);
        selectedCards = game.getGods();
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);

    }

    public ArrayList<String> getSelectedCards() {
        return selectedCards;
    }
}
