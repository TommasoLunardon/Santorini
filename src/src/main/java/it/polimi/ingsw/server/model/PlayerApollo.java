package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

import java.util.ArrayList;

public class PlayerApollo extends PlayerNotAthena {

   public PlayerApollo(String id, Integer age, PlayerColor color, Map map) {

       super(id, age, map, color, 4, "Apollo");
   }

   /**
    * Method to perform the special movement available only to players having Apollo as God
    * @param worker selected worker to perform the movement
    * @param nextBox selected Box to move in
    * @throws WrongMovementException if the movement isn't valid
    */
   public void move(Worker worker, Box nextBox) throws WrongMovementException, AthenaConditionException, InvalidMovementException, WorkerNotExistException, InvalidIndicesException {

       if (!checkFreeMovement() && worker.getBox().getLevel() < nextBox.getLevel()) {
           throw new AthenaConditionException();
       }

       if (!worker.getBox().getNeighbours().contains(nextBox) || nextBox.hasDome() || nextBox.getLevel() > worker.getBox().getLevel() + 1) {
           throw new WrongMovementException();
       }


       if(nextBox.hasWorker()) {

           if (this.getWorkers().contains(nextBox.getWorker())) {
               throw new WrongMovementException();
           }

           Worker enemy = nextBox.getWorker();

           Box oldBox = worker.getBox();

           nextBox.setWorker(worker);
           oldBox.setWorker(enemy);
           worker.setBox(nextBox);
           enemy.setBox(oldBox);

           if (worker.getBox().getLevel() == 3) {
               setWinner(true);
           }
       }
       else{
           super.move(worker,nextBox);
       }

   }

   @Override
   public boolean canMove() throws WorkerNotExistException {
       if(!super.canMove()){
           boolean check = false;
           for(int i = 0; i < getWorkers().size(); i++) {
               Box b = this.getWorkers().get(i).getBox();
               ArrayList<Box> neighbours = b.getNeighbours();
               for(int j = 0; j < neighbours.size(); j++) {
                   if (checkFreeMovement() && neighbours.get(j).hasWorker() && !this.getWorkers().contains(neighbours.get(j).getWorker()) && neighbours.get(j).getLevel() <= b.getLevel() + 1) {
                       check = true;
                   }

                   if (!checkFreeMovement() && neighbours.get(j).hasWorker() && !this.getWorkers().contains(neighbours.get(j).getWorker()) && neighbours.get(j).getLevel() <= b.getLevel()) {
                       check = true;
                   }
               }
           }
           return check;

       }
       else{
           return super.canMove();
       }
   }
}
