package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.AthenaConditionException;
import it.polimi.ingsw.server.model.exceptions.InvalidConstructionException;
import it.polimi.ingsw.server.model.exceptions.InvalidMovementException;

import java.util.ArrayList;

/**
 *
 * The class Player represents a player, it has a playerID that identifies a unique player.
 * @author Jing Huang, Tommaso Lunardon
 *
 */

public class Player {

    private final String playerID;
    private final int playerAge;
    private final PlayerColor color;
    private  Worker worker1;
    private  Worker worker2;
    protected final Map playerMap;
    private boolean isLoser;
    private boolean isWinner;



    public Player(String playerID, int playerAge, PlayerColor color, Map playerMap) {
        this.playerID = playerID;
        this.playerAge = playerAge;
        this.color = color;
        this.playerMap = playerMap;
        this.worker1 = null;
        this.worker2 = null;
        isLoser = false;
        isWinner = false;
    }

    public void setWorker1(Worker worker1){
        this.worker1 = worker1;
    }

    public void setWorker2(Worker worker2){
        this.worker2 = worker2;
    }

   public String getPlayerID(){
       return this.playerID;
   }


    public int getPlayerAge() {
        return playerAge;
    }

    public PlayerColor getColor(){
        return this.color;
    }

    /**
     *
     * @return the workers belonging to the player
     */
    public ArrayList<Worker> getWorkers(){

        ArrayList<Worker> workers = new ArrayList<Worker>();
        workers.add(worker1);
        workers.add(worker2);
      return workers;
    }

    public boolean isLoser() {
        return isLoser;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setLoser(boolean condition) {
        isLoser = condition;
    }

    public void setWinner(boolean condition) {
        isWinner = condition;
    }

    public Map getPlayerMap() {
        return playerMap;
    }

    /**
     *
     * @return true <==> the player is able to move with at least one worker
     */
    public boolean canMove(){
        boolean check = false;
        for (int i = 0; i<getWorkers().size(); i++){
            if(getWorkers().get(i).canMove()){check = true;}
        }
        return check;
    }

    /**
     *
     * @return true <==> the player is able to build with at least one worker
     */
    public boolean canBuild(){
        boolean check = false;
        for (int i = 0; i<getWorkers().size(); i++){
            if(getWorkers().get(i).canBuild()){check = true;}
        }
        return check;
    }

    /**
     *
     * @param worker is the worker that will perform the movement
     * @param nextBox is the box in which the worker will move
     * @throws InvalidMovementException if the movement isn't allowed
     */
    public void move(Worker worker, Box nextBox) throws InvalidMovementException, AthenaConditionException {

        if (!getWorkers().contains(worker)){ throw new InvalidMovementException();}

        try{
            worker.move(nextBox);
        }catch(Exception e){
            e.printStackTrace();
        }
        if (worker.isWinner()){
            setWinner(true);
        }
    }

    /**
     *
     * @param worker is the worker that will build
     * @param selectedBox is the box in which the worker will build a block
     * @throws InvalidConstructionException if this construction isn't allowed
     */
    public void build(Worker worker, Box selectedBox) throws InvalidConstructionException{
        if (!getWorkers().contains(worker)){ throw new InvalidConstructionException();}

        try{
            worker.build(selectedBox);
        }catch(Exception e){
            System.out.println(e);;
        }

    }


}