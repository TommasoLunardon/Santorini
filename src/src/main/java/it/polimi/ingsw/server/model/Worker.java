package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.exceptions.*;

import java.util.ArrayList;

/**
 * @author Tommaso Lunardon
 */

public class Worker {

    private Box box;
    private final Player player;
    private boolean isWinner;
    private boolean isLoser;

    /**
     * constructor for Worker
     * @param player = Player to which the worker belongs
     * @param box = box upon which the worker is
     */
    public Worker(Player player, Box box){
        this.player =  player;
        isLoser = false;
        isWinner = false;
        this.box = box;
        box.setWorker(this);
    }

    /**
     *
     * @return true <==> the worker can move to another box
     */
    public boolean canMove(){
        ArrayList<Box> neighbours = box.getNeighbours();
        boolean check = false;
        for (int i = 0; i<neighbours.size(); i++){
            if ( !neighbours.get(i).hasWorker() && !neighbours.get(i).hasDome() && !(neighbours.get(i).getLevel() > box.getLevel() +1)){
                check = true;
            }
        }

        return check;
    }

    /**
     *
     * @return true <==> the worker can build on another box
     */
    public boolean canBuild(){
        ArrayList<Box> neighbours = box.getNeighbours();
        boolean check = false;
        for (int i = 0; i<neighbours.size(); i++){
            if ( !neighbours.get(i).hasWorker() && !neighbours.get(i).hasDome()){
                check = true;
            }
        }


        return check;
    }

    /**
     *
     * @param nextBox = box where the worker will move
     * @throws WrongMovementException if the Box selected isn't valid
     */
    public void move(Box nextBox) throws WrongMovementException{
        ArrayList<Box> neighbours = box.getNeighbours();

        if (!neighbours.contains(nextBox) || nextBox.hasWorker() || nextBox.hasDome() || nextBox.getLevel() > box.getLevel() +1){
        throw new WrongMovementException();
        }

        else {
            nextBox.setWorker(this);
            this.box.removeWorker();
            this.setBox(nextBox);

            if (getBox().getLevel() == 3 && box.getLevel() < 3){
                this.isWinner = true;
            }
        }
    }

    /**
     *
     * @param selectedBox = box in which the worker will build a new level
     * @throws WrongConstructionException if  the selected box isn't valid
     */
    public void build(Box selectedBox) throws WrongConstructionException{

        ArrayList<Box> neighbours = box.getNeighbours();

        if (!neighbours.contains(selectedBox) || selectedBox.hasWorker() || selectedBox.hasDome()) {throw new WrongConstructionException();}

        int currentLevel = selectedBox.getLevel();
        try{
        selectedBox.setLevel(currentLevel+1);
        }catch(NotValidLevelException e){
            System.out.println(e);
        }
    }

    /**
     *
     * @return true <==> the worker reaches a winning condition
     */
    public boolean isWinner(){
        return isWinner;
    }

    /**
     *
     * @return true <==> the worker reaches a losing condition
     */
    public boolean isLoser(){
        return isLoser;
    }

    /**
     *
     * @return the box containing this worker
     */
    public Box getBox(){
        Box b = this.box;
        return b;
    }

    /**
     *
     * @return the Player to whom this worker belongs
     */
    public Player getPlayer(){
        Player p = player;
        return p;
    }

    /**
     *
     * @param box is the new box in which this worker will be placed
     */
    public void setBox(Box box){
        this.box = box;
    }

    /**
     * Method to check if the worker can move in a specific box
     * @param box box that needs to be checked
     * @return true <==> the worker can move in such box
     */
    public boolean canMove(Box box) throws WorkerNotExistException {
        boolean check = false;
        if (player instanceof PlayerNotAthena && !((PlayerNotAthena) player).checkFreeMovement()) {
            if (box.getLevel() <= getBox().getLevel() && !box.hasWorker() && !box.hasDome()) {
                check = true;
            }
        }

        if (player instanceof PlayerApollo && !((PlayerApollo) player).checkFreeMovement()) {
            if (box.getLevel() <= getBox().getLevel() && !box.hasWorker() && !box.hasDome()) {
                check = true;
            } else {
                if (box.hasWorker() && !player.getWorkers().contains(box.getWorker()) && box.getLevel() <= getBox().getLevel()) {
                    check = true;
                }

            }

        }
        if (player instanceof PlayerApollo && ((PlayerApollo) player).checkFreeMovement()) {
            if (!box.hasWorker() && !box.hasDome() && !(box.getLevel() > getBox().getLevel() + 1)) {
                check = true;
            } else {
                if (box.hasWorker() && !player.getWorkers().contains(box.getWorker()) && box.getLevel() <= getBox().getLevel() + 1) {
                    check = true;
                }

            }

        }

        if (player instanceof PlayerMinotaur && !((PlayerApollo) player).checkFreeMovement()) {
            if (box.getLevel() <= getBox().getLevel() && !box.hasWorker() && !box.hasDome()) {
                check = true;
            } else {
                boolean enemyNear = box.hasWorker() && !player.getWorkers().contains(box.getWorker()) && box.getLevel() <= getBox().getLevel();

                if (enemyNear) {
                    int dirX = box.getPosition()[0] - getBox().getPosition()[0];
                    int dirY = box.getPosition()[1] - getBox().getPosition()[1];
                    try {
                        Box nextBox2 = player.getPlayerMap().getBox(box.getPosition()[0] + dirX, box.getPosition()[1] + dirY);
                        if (!nextBox2.hasWorker() && !nextBox2.hasDome()) {
                            check = true;
                        }

                    } catch (InvalidIndicesException e) {
                    }

                }

            }
        }
            if (player instanceof PlayerMinotaur && ((PlayerApollo) player).checkFreeMovement()) {
                if (!box.hasWorker() && !box.hasDome() && !(box.getLevel() > getBox().getLevel() + 1)) {
                    check = true;
                } else {
                    boolean enemyNear = box.hasWorker() && !player.getWorkers().contains(box.getWorker()) && box.getLevel() <= getBox().getLevel() + 1;

                    if (enemyNear) {
                        int dirX = box.getPosition()[0] - getBox().getPosition()[0];
                        int dirY = box.getPosition()[1] - getBox().getPosition()[1];
                        try {
                            Box nextBox2 = player.getPlayerMap().getBox(box.getPosition()[0] + dirX, box.getPosition()[1] + dirY);
                            if (!nextBox2.hasWorker() && !nextBox2.hasDome()) {
                                check = true;
                            }

                        } catch (InvalidIndicesException e) {
                        }

                    }

                }

            } else {
                if (!box.hasWorker() && !box.hasDome() && !(box.getLevel() > getBox().getLevel() + 1)) {
                    check = true;
                }

            }
            return check;


        }
    }