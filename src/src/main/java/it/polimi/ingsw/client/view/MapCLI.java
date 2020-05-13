package it.polimi.ingsw.client.view;
import it.polimi.ingsw.server.model.PlayerColor;
import it.polimi.ingsw.server.model.Worker;

/**
 * these class create a set of box ordered in a matrix to simulate map conformation
 * @author Gabriele Gatti
 */

public class MapCLI extends Map {
    private BoxCLI[][] map;
    /**
     * Creates a MapCLI
     */
    public MapCLI(){
        this.map = new BoxCLI[5][5];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                this.map[x][y] = new BoxCLI(x, y);
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
        if (x>=0 && y>=0 && x<5 && y<5) {
            map[x][y].setWorker(color);
        }
        else {
            throw new InputFailedException();
        }
    }

    /**
     * build a structure in box (boxX, bobY)
     * @param boxX box's where build coordinate X
     * @param boxY box's where build coordinate Y
     */
    public void build(int boxX, int boxY){
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
     *move a worker to box (boxX, boxY)
     * @param boxXgo: box's coordinate X
     * @param boxYgo: box's coordinate Y
     * @param worker: worker to move
     */
    public void moveWorker(int boxXgo, int boxYgo, Worker worker){
        map[boxXgo][boxYgo].setWorker(worker.getPlayer().getColor());
        map[worker.getBox().getPosition()[0]][worker.getBox().getPosition()[1]].setBoxWhitOutWorker();
    }

    /**
     * map's screen printing
     */
    public void printMap() {
        for (int mapY = 4; mapY >= 0; mapY--){
                for(int boxY = 4; boxY >= 0; boxY--){
                    for (int mapX = 0; mapX <5 ; mapX++) {
                        map[mapX][mapY].printBox(boxY);
                    }
                    System.out.print("\n");
                }
        }
    }

    /**
     * @param x box's coordinate X
     * @param y box's coordinate Y
     * @return box(x,y)
     */
    public BoxCLI getBoxCLI(int x, int y) {
        return map[x][y];
    }
}