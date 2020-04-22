package it.polimi.ingsw.view;
import it.polimi.ingsw.server.model.PlayerColor;
import it.polimi.ingsw.server.model.Worker;

/**
 * these class create a set of box orded in a matrix for simulate map conformation
 * @author Gabriele Gatti
 */

public class MapCLI {
    private BoxCLI[][] map;

    /**
     * @param dimension box's dimensions
     * @throws InputFailedException for insertion of a wrong dimension
     */
    public MapCLI(int dimension) throws InputFailedException {
        this.map = new BoxCLI[5][5];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                this.map[x][y] = new BoxCLI(dimension);
            }
        }
    }

    /**
     * arrange a worker in box (x,y)
     * @param x moving box x-coordinate's
     * @param y moving box y-coordinate's
     * @param color worker's color
     * @throws InputFailedException for choose of a wrong box
     */
    public void setWorker(int x, int y, PlayerColor color) throws InputFailedException{
        if (x>=0 && y>=0 && x<5 && y<5) map[x][y].setWorker(color);
        else throw new InputFailedException();
    }

    /**
     * build a structure in box(boxX, bobY)
     * @param boxX box's where build coordinate X
     * @param boxY box's where build coordinate Y
     * @throws InputFailedException just for sceen
     */
    public void build(int boxX, int boxY) throws InputFailedException {
        map[boxX][boxY].buildStructure();
    }

    /**
     * build a dome in box(boxX,boxY)
     * @param boxX box's where build coordinate X
     * @param boxY box's where build coordinate Y
     */
    public void buildDome(int boxX, int boxY){
        map[boxX][boxY].buildDome();
    }

    /**
     *move a worker
     * @param boxXgo: box's where go coordinate X
     * @param boxYgo: box's where go coordinate Y
     * @param worker: worker to move
     * @throws InputFailedException if box's where go have problems
     */
    public void moveWorker(int boxXgo, int boxYgo, Worker worker) throws InputFailedException {
        map[boxXgo][boxYgo].setWorker(worker.getPlayer().getColor());
        map[worker.getBox().getPosition()[0]][worker.getBox().getPosition()[1]].leftWorker();
    }

    /**
     * map's screen printing
     */
    public void printMap(){
        System.out.print("\t\t\t\t\t\t");
        for(int i=0; i< 5.;i++){
            for(int j=0 ; j<5 ; j++){
                System.out.print(map[j][i]);
            }
            System.out.print("\n\t\t\t\t\t\t");
        }
    }

    /**
     * @param x box's coordinate X
     * @param y box's coordinate Y
     * @return box(x,y)n
     */
    public BoxCLI getBxCLI(int x, int y) {
        return map[x][y];
    }
}