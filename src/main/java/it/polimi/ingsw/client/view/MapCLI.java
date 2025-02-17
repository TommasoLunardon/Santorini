package it.polimi.ingsw.client.view;
import it.polimi.ingsw.server.model.PlayerColor;
import it.polimi.ingsw.server.model.Worker;

/**
 * Class used to represent the game's map for the client
 */

public class MapCLI extends Map {
    private BoxCLI[][] map;

    public MapCLI (){
        map=new BoxCLI[5][5];
        for (int y=0; y<5; y++){
            for (int x=0; x<5; x++) {
                map[x][y]= new BoxCLI();
                System.out.println();
            }
        }
    }

    /**
     * Method used to build in a box
     * @param x box coordinate x
     * @param y box coordinate y
     */
    public void build(int x,int y){
        map[x][y].buildStructure();
    }

    /**
     *
     * @param x box coordinate x
     * @param y box coordinate y
     * @return the box at position (x,y)
     */
    public BoxCLI getBoxCLI(int x, int y){
        return map[x][y];
    }

    /**
     * Method used to represent graphically the player's color
     * @param color is the player's color
     * @return the color name
     */
    private String catchColor(PlayerColor color){
        String colorW;
        if (color==PlayerColor.BLUE){
            colorW="\u001b[38;5;21m";
        }else {
            if (color==PlayerColor.RED){
                colorW="\u001b[38;5;1m";
            }else {
                colorW = "\u001b[38;5;220m";
            }
        }
        return colorW;
    }

    /**
     * Method used to set a worker in the box (x,y)
     * @param x box's coordinate x
     * @param y box's coordinate y
     * @param color worker's color
     */
    public void setWorker(int x, int y, PlayerColor color){
        map[x][y].setWorker(catchColor(color));
    }

    /**
     * Method used to move a worker on the map
     * @param boxXgo coordinate x of box to reach
     * @param boxYgo coordinate y of box to reach
     * @param worker is the worker to be moved
     * @throws InputFailedException if the coordinates aren't valid
     */
    public void moveWorker(int boxXgo, int boxYgo, Worker worker) throws InputFailedException{
        if (boxXgo>=0&&boxYgo>=0&&boxXgo<5&&boxYgo<5){
            map[boxXgo][boxYgo].setWorker(catchColor(worker.getPlayer().getColor()));
            map[worker.getBox().getPosition()[0]][worker.getBox().getPosition()[1]].leaveWorker();
        }
        else {
            throw new InputFailedException();
        }
    }

    /**
     * Method used to print the game's map on the screen
     */
    public void printMap(){
        for (int y=0; y<5; y++) {
            for (int l = 0; l < 5; l++) {
                for (int x = 0; x < 5; x++) {
                    map[x][y].printBox(l);
                }
                System.out.print("\n");
            }
        }
        System.out.println();
    }
}