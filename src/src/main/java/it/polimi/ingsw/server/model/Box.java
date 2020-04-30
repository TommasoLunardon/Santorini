package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.exceptions.NotValidBoxException;
import it.polimi.ingsw.server.model.exceptions.NotValidLevelException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Tommaso Lunardon
 */


public class Box implements Serializable {
    private int level;
    private final int positionX;
    private final int positionY;
    private boolean hasDome;
    private boolean hasWorker;
    private final Map map;
    private Worker worker;


    /**
     *
     * @param x = box latitude
     * @param y = box longitude
     * @param map = Map containing the box
     * @throws NotValidBoxException if position(x,y) doesn't belong to the Map matrix, or the Map doesn't exist
     */
    public Box(int x, int y, Map map)throws NotValidBoxException{
        if (x<0 || y<0 || x>4 || y>4 || map == null) {throw new NotValidBoxException();}
        level = 0;
        positionX = x;
        positionY = y;
        hasDome = false;
        hasWorker = false;
        this.map = map;
        worker = null;
    }

    /**
     *
     * @param box
     * @return true <==> this.box and box are equal
     */
    public boolean equals(Box box) throws NullPointerException{
        if(box == null) {throw new NullPointerException();}
        boolean check = true;
        if(box.map!=map || box.getPosition()[0] != positionX || box.getPosition()[1] != positionY){
            check = false;
        }

        return check;
    }
    /**
     *
     * @return true <==> there is a worker on the box
     */
    public boolean hasWorker(){
        return hasWorker;
    }

    /**
     *
     * @return current box's level
     */
    public int getLevel() {
        int l = level;
        return l;
    }

    /**
     *
     * @return position (x,y) of the box in the map's matrix
     */
    public int[] getPosition(){
        int[] pos = {positionX , positionY};
        return pos;
    }

    /**
     *
     * @return the box's neighbours in matrix form ( Box[3][3]), where the current box is in position (1)(1)
     */
    public Box[][] getNeighboursMatrix(){

        Box[][] neighboursMatrix = new Box[3][3];

        boolean left = (positionX - 1 >= 0);
        boolean right = (positionX + 1 < 5);
        boolean up = (positionY+1 < 5);
        boolean down = (positionY-1 >= 0);

        try{
        if (left)  neighboursMatrix[0][1] = (map.getBox(positionX -1, positionY));} catch (Exception e) {
            System.out.println(e);;
        }
        try{
        if (right)  neighboursMatrix[2][1] = (map.getBox(positionX +1, positionY));}catch (Exception e) {
            System.out.println(e);;
        }
        try{
        if (up)  neighboursMatrix[1][2] = (map.getBox(positionX , positionY +1));}catch (Exception e){
            System.out.println(e);;
        }
        try{
        if (down) neighboursMatrix[1][0] = (map.getBox(positionX, positionY-1));}catch (Exception e){
            System.out.println(e);;
        }

        try{
        if (left && down) neighboursMatrix[0][0] = (map.getBox(positionX - 1, positionY-1));}catch (Exception e){
            System.out.println(e);;
        }
        try{
        if (left && up)  neighboursMatrix[0][2] = (map.getBox(positionX -1, positionY +1));}catch (Exception e){
            System.out.println(e);;
        }
        try{
        if (right && down)  neighboursMatrix[2][0] = (map.getBox(positionX +1, positionY-1));}catch (Exception e){
            System.out.println(e);;
        }
        try{
        if (right && up)  neighboursMatrix[2][2] = (map.getBox(positionX +1, positionY +1));}catch (Exception e){
            System.out.println(e);;
        }


        return neighboursMatrix;
    }


    /**
     *
     * @return the box's neighbours in an ArrayList
     */
    public ArrayList<Box> getNeighbours() {
        ArrayList<Box> neighbours = new ArrayList<Box>();

        boolean left = (positionX - 1 >= 0);
        boolean right = (positionX + 1 < 5);
        boolean up = (positionY+1 < 5);
        boolean down = (positionY-1 >= 0);

        try{
        if (left)  neighbours.add(map.getBox(positionX -1, positionY));}catch (Exception e){
            System.out.println(e);;
        }
        try{
        if (right)  neighbours.add(map.getBox(positionX +1, positionY));}catch (Exception e){
            System.out.println(e);;
        }

        try{
        if (up)  neighbours.add(map.getBox(positionX , positionY +1));}catch (Exception e){
            System.out.println(e);;
        }

        try{
        if (down) neighbours.add(map.getBox(positionX, positionY-1));}catch (Exception e){
            System.out.println(e);;
        }

        try{
        if (left && down) neighbours.add(map.getBox(positionX - 1, positionY-1));}catch (Exception e){
            System.out.println(e);;
        }
        try{
        if (left && up)  neighbours.add(map.getBox(positionX -1, positionY +1));}catch (Exception e){
            System.out.println(e);;
        }

        try{
        if (right && down)  neighbours.add(map.getBox(positionX +1, positionY-1));}catch (Exception e){
            System.out.println(e);;
        }

        try{
        if (right && up)  neighbours.add(map.getBox(positionX +1, positionY +1));}catch (Exception e){
            System.out.println(e);;
        }


        return neighbours;
    }

    /**
     *
     * @return true <==> the box contains a Dome
     */
    public boolean hasDome(){
        return hasDome;
    }

    /**
     *
     * @return the worker on the box
     * @throws WorkerNotExistException if the box doesn't have any worker
     */
    public Worker getWorker() throws WorkerNotExistException{

        if(!hasWorker()) { throw new WorkerNotExistException();}

        else {
            Worker w = worker;
            return w;
        }

    }


    /**
     *
     * @param level = new box's level
     * @throws NotValidLevelException if the new level isn't acceptable by the game's rules
     */
    public void setLevel(int level) throws NotValidLevelException {
        if (level < 0 || level >4) throw new NotValidLevelException();
        this.level = level;
        if (level==4){
            setDome(true);
        }
    }

    /**
     *
     * @param hasDome indicates whether the box will or won't have a Dome
     */
    public void setDome(boolean hasDome) {
        this.hasDome = hasDome;
    }

    /**
     *
     * @param worker is the worker that will be on the box
     * @throws NullPointerException if the worker doesn't exist
     */
    public void setWorker(Worker worker) throws NullPointerException{
        if (worker == null) throw new NullPointerException();

        else {
            this.worker = worker;
            this.hasWorker = true;
        }
    }

    /**
     * removes the previous worker from the box
     */
    public void removeWorker(){
        this.hasWorker = false;
        this.worker = null;
    }

    public Map getMap() {
        return map;
    }
}

