package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

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

        if(!box.getMap().equals(playerMap)|| box.hasDome()||box.hasWorker()){throw new InvalidBoxException();}

        Worker worker1 = new Worker(this,box);

        this.worker1 = worker1;
    }

    public void setWorker2(Box box)throws NullPointerException, InvalidBoxException{
        if(box==null){throw new NullPointerException();}

        if(!box.getMap().equals(playerMap)|| box.hasDome()||box.hasWorker()){throw new InvalidBoxException();}

        Worker worker2 = new Worker(this,box);


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
     * @throws InvalidMovementException if the worker can't be chosen
     */
    public void move(Worker worker, Box nextBox) throws InvalidMovementException, AthenaConditionException {

        if (!getWorkers().contains(worker)){ throw new InvalidMovementException();}

        try{
            worker.move(nextBox);
        }catch(WrongMovementException e){
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
     * @throws InvalidConstructionException if the worker can't be chosen
     */
    public void build(Worker worker, Box selectedBox) throws InvalidConstructionException{
        if (!getWorkers().contains(worker)){ throw new InvalidConstructionException();}

        try{
            worker.build(selectedBox);
        }catch(WrongConstructionException e){
            System.out.println(e);;
        }

    }


}