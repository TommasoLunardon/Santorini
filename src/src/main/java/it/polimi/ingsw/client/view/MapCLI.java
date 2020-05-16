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
    public MapCLI (){
        map=new BoxCLI[5][5];
        for (int y=0; y<5; y++){
            for (int x=0; x<5; x++) {
                map[x][y]= new BoxCLI();
                System.out.println();
            }
        }
    }


    public void build(int x, int y){
        map[x][y].buildStructure();
    }

    public void buildDome(int x, int y){
        map[x][y].buildDome();
    }

    public BoxCLI getBoxCLI(int x, int y){
        return map[x][y];
    }

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

    public void setWorker(int x, int y, PlayerColor color) throws InputFailedException {
        if (x<0||x>5||y<0||y>5) {
            map[x][y].setWorker(catchColor(color));
        }
        else {
            throw new InputFailedException();
        }
    }

    public void moveWorker(int boxXgo, int boxYgo, Worker worker) throws InputFailedException{
        if (boxXgo>=0&&boxYgo>=0&&boxXgo<5&&boxYgo<5){
            map[boxXgo][boxYgo].setWorker(catchColor(worker.getPlayer().getColor()));
            map[worker.getBox().getPosition()[0]][worker.getBox().getPosition()[1]].leaveWorker();
        }
        else {
            throw new InputFailedException();
        }
    }

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