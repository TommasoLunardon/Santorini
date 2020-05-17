package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class Player represents a Santorini's player, it has a playerID that uniquely identifies player.
 * @author Jing Huang, Tommaso Lunardon
 */

public class Player implements Serializable {

    private final String playerID;
    private final int playerAge;
    private final PlayerColor color;
    private  Worker worker1;
    private  Worker worker2;
    final Map playerMap;
    private boolean isLoser;
    private boolean isWinner;


    public Player(String playerID, int playerAge, PlayerColor color, Map playerMap) throws NullPointerException {

        if(playerID == null|| color == null||playerMap==null){throw new NullPointerException();}

        this.playerID = playerID;
        this.playerAge = playerAge;
        this.color = color;
        this.playerMap = playerMap;
        this.worker1 = null;
        this.worker2 = null;
        isLoser = false;
        isWinner = false;
    }

    public void setWorker1(Box box) throws NullPointerException, InvalidBoxException {
        if(box==null){throw new NullPointerException();}

        if(!box.getMap().equals(playerMap)||box.hasDome()||box.hasWorker()){throw new InvalidBoxException();}

        this.worker1 = new Worker(this,box);
    }

    public void setWorker2(Box box)throws NullPointerException, InvalidBoxException{
        if(box==null){throw new NullPointerException();}

        if(!box.getMap().equals(playerMap)|| box.hasDome()||box.hasWorker()){throw new InvalidBoxException();}


        this.worker2 = new Worker(this,box);
    }

   public String getPlayerID(){
       return playerID;
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

    /**
     * Method used to remove the player's workers from the map
     * @throws WorkerNotExistException if the player doesn't have any workers on the map
     */
    void removeWorkers() throws  WorkerNotExistException{

        if(worker1==null||worker2==null) {throw new WorkerNotExistException();}

        getWorkers().get(0).getBox().removeWorker();
        getWorkers().get(1).getBox().removeWorker();
    }

    public boolean isWinner() {
        return isWinner;
    }

    void setWinner(boolean condition) {
        isWinner = condition;
    }

    Map getPlayerMap() {
        Map m = playerMap;
        return m;
    }

    /**
     * @return true <==> the player is able to move with at least one worker
     */
    public boolean canMove() throws WorkerNotExistException {
        boolean check = false;
        for (int i = 0; i<getWorkers().size(); i++){
            if(getWorkers().get(i).canMove()){check = true;}
        }
        return check;
    }

    /**
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
     * Method used to perform a worker's movement
     * @param worker is the worker that will perform the movement
     * @param nextBox is the box in which the worker will move
     * @throws InvalidMovementException if the worker can't be chosen
     */
    public void move(Worker worker, Box nextBox) throws InvalidMovementException, AthenaConditionException, WrongMovementException, WorkerNotExistException, InvalidIndicesException {

        if (!getWorkers().contains(worker)){ throw new InvalidMovementException();}

            worker.move(nextBox);

        if (worker.isWinner()){
            setWinner(true);
        }
    }

    /**
     * Method used to perform a worker's construction
     * @param worker is the worker that will build
     * @param selectedBox is the box in which the worker will build a block
     * @throws InvalidConstructionException if the worker can't be chosen
     */
    public void build(Worker worker, Box selectedBox) throws InvalidConstructionException, WrongConstructionException {
        if (!getWorkers().contains(worker)){ throw new InvalidConstructionException();}

            worker.build(selectedBox);
    }

}