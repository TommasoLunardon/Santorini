package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.NotValidPlayersException;
import it.polimi.ingsw.server.model.exceptions.NotValidWorkersException;
import it.polimi.ingsw.server.model.exceptions.PlayerNotValidException;
import it.polimi.ingsw.server.model.exceptions.WrongPlayers_WorkersException;

import java.util.ArrayList;

/**
 * @author Tommaso Lunardon
 */


public class CurrentTurn {
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Worker> workers = new ArrayList<Worker>();
    private Map map;
    private Player activePlayer;


    public CurrentTurn(Map map) throws NullPointerException {
        if(map ==null) {throw new NullPointerException();}

        this.players = new ArrayList<>();
        this.workers = new ArrayList<>();
        this.map = map;

    }

    /**
     *
     * @return Players in the game at the current turn
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> x = players;
        return x;
    }



    /**
     *
     * @return Workers in the game at the current turn
     */
    public ArrayList<Worker> getWorkers() {
        ArrayList<Worker> x = workers;
        return x;
    }

    /**
     *
     * @return The game's Map
     */
    public Map getMap() {
        Map m = map;
        return m;
    }

    /**
     *
     * @param activePlayer sets the player that is performing his/her turn
     * @throws PlayerNotValidException if the Player doesn't belong to the game
     */
    public void setActivePlayer(Player activePlayer) throws PlayerNotValidException{

        if(!getPlayers().contains(activePlayer)){throw new PlayerNotValidException();}

        this.activePlayer = activePlayer;
    }

    /**
     *
     * @return the active player
     */
    public Player getActivePlayer(){
        Player active = activePlayer;
        return active;
    }

    /**
     * Method to remove a player from the game
     * @param p is the player to be removed
     */
    public void removePlayer(Player p) {
        players.remove(p);
        ArrayList<Worker> w = p.getWorkers();

        for(int i=0; i<w.size(); i++){
            workers.remove(w.get(i));
        }
    }
    /**
     * Method to insert players in the game with their workers
     * @param p is the player added to the game
     */
    public void addPlayer(Player p) throws  NotValidPlayersException{

        if(!p.getPlayerMap().equals(map)){
            throw new NotValidPlayersException();
        }

        players.add(p);
        ArrayList<Worker> w = p.getWorkers();

        for(int i=0; i<w.size(); i++){
            workers.add(w.get(i));
        }
    }
}
