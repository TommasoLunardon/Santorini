package it.polimi.ingsw.server.model;

/**
 * Class Pair used to couple each player with his or her unique ID.
 */

import java.io.Serializable;

public class Pair implements Serializable {
    private Player player;
    private String ID;


    Pair(Player player, String ID) {
        this.player = player;
        this.ID = ID;
    }

    public Player getPlayer() {
        return player;
    }

    String getID() {
        return ID;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
