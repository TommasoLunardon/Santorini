package it.polimi.ingsw.server.model;

/**
 *
 * The class Player represents a player, it has a playerID that identifies a unique player.
 * @author Jing Huang
 *
 */

public class Player {

    private String playerID;
    private int playerAge;
    private PlayerColor color;
    private transient Worker worker;
    private final Map playerMap;
    private transient Worker move;
    private transient Box costruction;


    public Player(String playerID, int playerAge) {
        this.playerID = playerID;
        this.playerAge = playerAge;
        this.color = null;
        this.playerMap = new Map();
        this.worker = null;
        move = null;
        costruction = null;
    }
    public Player(String playerID, int playerAge, PlayerColor color, Map playerMap) {
        this.playerID = playerID;
        this.playerAge = playerAge;
        this.color = color;
        this.playerMap = playerMap;
        this.worker = worker;
        move = null;
        costruction = null;
    }

    public void getPlayerID(String playerID) {
        this.playerID = playerID;
    }
    public void getPlayerAge(Integer playerAge) {
        this.playerAge = playerAge;
    }
    public PlayerColor getColor() {
        return this.color;
    }
    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public Worker getWorker(Worker worker){
       this.worker = worker;
    }

    public Map getPlayerMap() {
        return this.playerMap;
    }
    public Worker getmove() {
        return move;
    }
    public Box getCostruction() {
        return costruction;
    }



}