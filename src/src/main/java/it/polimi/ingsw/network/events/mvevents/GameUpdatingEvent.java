package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;

public class GameUpdatingEvent extends MVEvent {

    private Game game;

    public GameUpdatingEvent(String target, Game game) {
        super(target);
        this.game = game;
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);

    }

    public Game getGame() {
        Game g = game;
        return g;
    }
}
