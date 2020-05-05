package it.polimi.ingsw.server.model;

/**
 * Enum Class to distinguish the players in different teams depending on their color
 */
public enum  PlayerColor {
    RED,BLUE,YELLOW;


    @Override
    public String toString() {
        if(this.equals(RED)){return "Red";}
        if(this.equals(YELLOW)){return "Yellow";}
        if(this.equals(BLUE)){return "Blue";}
        return "";
    }
}
