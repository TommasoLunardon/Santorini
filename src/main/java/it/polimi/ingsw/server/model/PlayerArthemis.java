package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
/**
 * The class PlayerArthemis represents a player using Arthemis as god
 */
public class PlayerArthemis extends PlayerNotAthena {

            public PlayerArthemis(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Arthemis");
            }

            /**
             * Method to perform the special movement available to players with Arthemis as God,
             * in addition to the standard movement method
             * @param nextBox1 first box to move in
             * @param nextBox2 second box to move in
             * @param worker selected worker to perform the movement
             * @throws WrongMovementException if the movement isn't valid
             */
            public void moveArthemis(Box nextBox1, Box nextBox2, Worker worker) throws WrongMovementException, InvalidMovementException, WorkerNotExistException, InvalidIndicesException, AthenaConditionException {

                    Box initialBox = worker.getBox();
                if (nextBox2.equals(initialBox)) {
                    throw new WrongMovementException();
                }
                    super.move(worker, nextBox1);
                try {
                   super.move(worker, nextBox2);
                }catch(WrongMovementException | AthenaConditionException e){
                    super.move(worker, initialBox);
                    throw new WrongMovementException();
                }

            }


        }
