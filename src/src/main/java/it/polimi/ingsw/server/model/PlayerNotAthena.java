package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

import java.util.ArrayList;

public class PlayerNotAthena extends PlayerDivinity{

    private boolean freeMovement;

    public PlayerNotAthena(String id, Integer age, Map map, PlayerColor color, Integer nPlayer, String godName){

        super(id, age, map, color,  nPlayer, godName);

        freeMovement = true;
    }

    /**
     * Override of the movement method, to satisfy the eventual Athena's restriction
     * @param worker is the worker that will perform the movement
     * @param nextBox is the box in which the worker will move
     * @throws AthenaConditionException if the Athena Condition isn't followed
     */
    public void move(Worker worker, Box nextBox) throws AthenaConditionException, InvalidMovementException, WrongMovementException, WorkerNotExistException, InvalidIndicesException {
        if (checkFreeMovement()){

            super.move(worker,nextBox);
        }
        else{
            int oldLevel = worker.getBox().getLevel();

            worker.move(nextBox);

            if(oldLevel < worker.getBox().getLevel()){
                throw new AthenaConditionException();
            }

        }
    }

    /**
     * Method used to update the Athena Condition (Pattern Observer)
     * @param condition
     */
    public void update(boolean condition){

        freeMovement = condition;
    }

    protected Boolean checkFreeMovement(){

        Boolean p = this.freeMovement;
        return p;
    }

    @Override
    public boolean canMove() throws WorkerNotExistException {
        if(!checkFreeMovement() && super.canMove()){
            boolean check = false;
            for(int i = 0; i < getWorkers().size(); i++) {
                Box b = this.getWorkers().get(i).getBox();
                ArrayList<Box> neighbours = b.getNeighbours();
                for(int j = 0; j < neighbours.size(); j++){
                    if(neighbours.get(j).getLevel() <= b.getLevel() && !neighbours.get(j).hasWorker() && !neighbours.get(j).hasDome() ){check = true;}
                }
            }
            return check;

        }
        else{return super.canMove();}
    }
}
