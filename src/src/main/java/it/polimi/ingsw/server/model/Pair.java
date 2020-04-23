package it.polimi.ingsw.server.model;

/*
   Class Pair used to couple each player with his or her unique ID.
 */


public class Pair {
    private Player player;
    private String ID;


    public Pair(Player player, String ID) {
        this.player = player;
        this.ID = ID;
    }

    public Player getPlayer() {
        return player;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
