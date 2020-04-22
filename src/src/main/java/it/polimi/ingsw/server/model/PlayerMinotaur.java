package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

import java.util.ArrayList;

public class PlayerMinotaur extends PlayerNotAthena {

            public PlayerMinotaur(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Minotaur");
            }

            /**
             * Method to perform the special movement available to players with Minotaur as god
             *
             * @param worker  is the selected worker to perform the movement
             * @param nextBox is the selected box to move in
             * @throws WrongMovementException if the movement isn't valid
             */
            public void move(Worker worker, Box nextBox) throws WrongMovementException, WorkerNotExistException, InvalidIndicesException, InvalidMovementException, AthenaConditionException {

                if (!checkFreeMovement() && worker.getBox().getLevel() < nextBox.getLevel()) {
                    throw new WrongMovementException();
                }

                if (!worker.getBox().getNeighbours().contains(nextBox) || nextBox.hasDome() || nextBox.getLevel() > worker.getBox().getLevel() + 1) {
                    throw new WrongMovementException();
                }

                if (nextBox.hasWorker()) {
                    if (this.getWorkers().contains(nextBox.getWorker())) {
                        throw new WrongMovementException();
                    }


                    Worker enemy = nextBox.getWorker();
                    Box oldBox = worker.getBox();
                    int dirX = nextBox.getPosition()[0] - oldBox.getPosition()[0];
                    int dirY = nextBox.getPosition()[1] - oldBox.getPosition()[1];
                    Box nextBox2 = playerMap.getBox(nextBox.getPosition()[0] + dirX, nextBox.getPosition()[1] + dirY);

                    if (nextBox2.hasWorker() || nextBox2.hasDome()) {
                        throw new WrongMovementException();
                    }

                    nextBox.setWorker(worker);
                    nextBox2.setWorker(enemy);
                    worker.setBox(nextBox);
                    enemy.setBox(nextBox2);
                    oldBox.removeWorker();

                    if (worker.getBox().getLevel() == 3) {
                        setWinner(true);
                    }

                } else {
                    super.move(worker, nextBox);
                }

            }


            @Override
            public boolean canMove() throws WorkerNotExistException {
                if (!super.canMove()) {
                    boolean check = false;
                    for (int i = 0; i < getWorkers().size(); i++) {
                        Box b = this.getWorkers().get(i).getBox();
                        ArrayList<Box> neighbours = b.getNeighbours();
                        for (int j = 0; j < neighbours.size(); j++) {

                            if(checkFreeMovement()) {
                                boolean enemyNear = neighbours.get(j).hasWorker() && !this.getWorkers().contains(neighbours.get(j).getWorker()) && neighbours.get(j).getLevel() <= b.getLevel() + 1;

                                if (enemyNear) {
                                    int dirX = neighbours.get(j).getPosition()[0] - b.getPosition()[0];
                                    int dirY = neighbours.get(j).getPosition()[1] - b.getPosition()[1];
                                    try {
                                        Box nextBox2 = playerMap.getBox(neighbours.get(j).getPosition()[0] + dirX, neighbours.get(j).getPosition()[1] + dirY);
                                        if (!nextBox2.hasWorker() && !nextBox2.hasDome()) {
                                            check = true;
                                        }

                                    } catch (InvalidIndicesException e) {
                                    }

                                }
                            }else{
                                boolean enemyNear = neighbours.get(j).hasWorker() && !this.getWorkers().contains(neighbours.get(j).getWorker()) && neighbours.get(j).getLevel() <= b.getLevel();

                                if (enemyNear) {
                                    int dirX = neighbours.get(j).getPosition()[0] - b.getPosition()[0];
                                    int dirY = neighbours.get(j).getPosition()[1] - b.getPosition()[1];
                                    try {
                                        Box nextBox2 = playerMap.getBox(neighbours.get(j).getPosition()[0] + dirX, neighbours.get(j).getPosition()[1] + dirY);
                                        if (!nextBox2.hasWorker() && !nextBox2.hasDome()) {
                                            check = true;
                                        }

                                    } catch (InvalidIndicesException e) {
                                    }

                                }


                            }
                        }
                    }
                    return check;

                } else {return super.canMove();
                }
            }
        }
