package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

import java.util.ArrayList;
/**
 * The class PlayerAthena represents a player using Athena as god
 */
public class PlayerAthena extends PlayerDivinity {

            private ArrayList<PlayerNotAthena> observer = new ArrayList<PlayerNotAthena>();

            public ArrayList<PlayerNotAthena> getObserver() {
                return observer;
            }

            public PlayerAthena(String id, Integer age, PlayerColor color, Map map) {
                super(id, age, map, color, 4, "Athena");
            }

            /**
             *Method used to assign other players as Observers of Athena Condition
             * @param observersList is the list of players playing the same game as the one having Athena as god
             */
            void attach(ArrayList<PlayerNotAthena> observersList) {
                for (int x = 0; x < observersList.size(); x++) {
                    observer.add(x, observersList.get(x));
                }
            }

            /**
             * Method that overrides the standard movement and assigns the Athena Condition if
             * the level is increased
             * @param worker is the worker that will perform the movement
             * @param nextBox is the box in which the worker will move
             */
            public void move( Worker worker, Box nextBox) throws InvalidMovementException, AthenaConditionException, WrongMovementException, WorkerNotExistException, InvalidIndicesException {
                int oldLevel = worker.getBox().getLevel();
                    super.move(worker,nextBox);

                    if (worker.getBox().getLevel() > oldLevel) {
                        notifyPlayers(false);
                    }
                    else {
                        notifyPlayers(true);
                    }


            }

            /**
             * Method used to update the Athena Condition and notify the Observers
             * @param condition is the updated AthenaCondition
             */
            private void notifyPlayers(boolean condition) {
                for (PlayerNotAthena playerNotAthena : observer) {
                    playerNotAthena.update(condition);
                }
            }
        }
