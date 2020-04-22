package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;

public class GameCreatedEvent extends MVEvent {
    private int num;
    private boolean withGods;

    public GameCreatedEvent(String target, Game game) {
        super(target);
        num = game.getNumPlayers();
        withGods = game.isWithGods();
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);


    }

    public boolean getWithGods() {
        return withGods;
    }

    public int getNum() {
        return num;
    }
}
