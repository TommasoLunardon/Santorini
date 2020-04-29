package it.polimi.ingsw.network.events.mvevents;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

public class PlayerJoinedEvent extends MVEvent {

    private Player player;

    public PlayerJoinedEvent(String target, Game game) {
        super(target);
        player = game.getPlayers().get(game.getPlayers().size() - 1);
    }

    @Override
    public void manage(MVEventSender eventSender) {
        eventSender.send(this);
    }

    public Player getP() {
        Player p = player;
        return p;
    }
}
