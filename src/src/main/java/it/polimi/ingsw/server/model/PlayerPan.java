package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
/**
 * The class PlayerPan represents a player using Pan as god
 */
public class PlayerPan extends PlayerNotAthena {

            public PlayerPan(String id, Integer age, PlayerColor color, Map map) {
                super(id, age, map, color, 4, "Pan");
            }

            /**
             * Method used to perform a movement that takes into account the additional win condition
             * assigned to players with Pan as God
             * @param worker is the worker that will perform the movement
             * @param nextBox is the box in which the worker will move
             * @throws InvalidMovementException if the movement isn't valid
             */
            public void move(Worker worker, Box nextBox) throws InvalidMovementException, WrongMovementException, AthenaConditionException, WorkerNotExistException, InvalidIndicesException {
                int oldLevel = worker.getBox().getLevel();

                    super.move(worker, nextBox);

                if (worker.getBox().getLevel() <= oldLevel - 2) {
                    setWinner(true);
                }

            }

        }
