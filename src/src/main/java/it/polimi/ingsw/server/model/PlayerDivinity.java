package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

import java.util.ArrayList;

/**
 * @author Gabriele Gatti, Tommaso Lunardon
 * Subclasses of Player, used to play games with Divinity Cards
 */

    public class PlayerDivinity extends Player{
    private String godName;
    private Integer nPlayer;

    public PlayerDivinity(String id, Integer age, Map map, PlayerColor color, Integer nPlayer, String godName){
        super(id, age, color, map);
        this.nPlayer = nPlayer;
        this.godName = godName;
    }

    /**
     *
     * @return The name of the God's Card assigned to the player
     */
    public String getGodName(){

        return godName;
    }

    /**
     *
     * @return the  accepted number of players in the game for the specific card used
     */
    public Integer getnPlayer(){
        int nPlayer1 = this.nPlayer;
        return nPlayer1;
    }
}


