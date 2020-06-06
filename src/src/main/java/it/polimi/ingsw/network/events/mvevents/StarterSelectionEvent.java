package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;
/**
 * Event that communicates to the client who is the starter
 */
public class StarterSelectionEvent extends MVEvent {

    private String starter;

    public StarterSelectionEvent(String target, Game game) {
        super(target);
        starter = game.getPlayers().get(0).getPlayerID();
    }

    @Override
    public void manage(MVEventSender eventSender) {

    }

    public String getStarter() {
        return starter;
    }
}
