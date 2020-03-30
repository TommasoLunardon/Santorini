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
    private transient PlayerDivinity playerDivinity;
    private transient Worker worker;
    private final Box playerBox;
    private transient Box move;
    private transient Box costruction;


    public Player(String playerID, int playerAge) {
        this.playerID = playerID;
        this.playerAge = playerAge;
        this.color = null;
        this.playerBox = new playerBox();
        this.worker = null;
        playerDivinity = null;
        move = null;
        costruction = null;
    }
    public Player(String playerID, int playerAge, PlayerColor color, Box playerBox) {
        this.playerID = playerID;
        this.playerAge = playerAge;
        this.color = color;
        this.playerBox = playerBox;
        this.worker = worker;
        playerDivinity = null;
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
    public Box getPlayerBox() {
        return this.playerBox;
    }
    public Worker getWorker(Worker worker){
       this.worker = worker;
    }

    public PlayerDivinity getPlayerDivinity() {
        return playerDivinity;
    }
    public Box getmove() {
        return move;
    }
    public Box getCostruction() {
        return costruction;
    }



}