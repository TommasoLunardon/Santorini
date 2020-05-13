package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.PlayerColor;

/**
 * Class used to control the specific visual unit box
 * @author Gabriele Gatti
 */

public class BoxCLI extends Box {
    private String[][] box;
    private static int dimension = 5;
    private int level;
    private boolean hasWorker;
    private PlayerColor playerColor;
    private int coordinateX, coordinateY;


    public int getLevel(){
        return level;
    }

    /**
     * Creation of BoxCLI
     */
    public BoxCLI(int coordinateX, int coordinateY){

            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
            this.box = new String[dimension][dimension];
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    if (y == 0 || x == 0 || x == dimension - 1 || y == dimension - 1 ) {
                        this.box[x][y] = "\u001b[48;5;0m" + "\u001b[38;5;326m" + "▉";
                    } else{
                        this.box[x][y] = "\u001b[48;5;22m" + "\u001b[38;5;28m" + "▉";
                    }
                }
            }

    }

    /**
     *
     * @return return box's matrix
     */
    /*
    public String[][] getBox() {
        return box;
    }
    */

    /**
     * paint a colored worker upon a building
     * @param colorWorker: string color
     */
    private void setColorWorker(String colorWorker, boolean onGrass) {
        int middle = dimension/2;
        String land;
        if (onGrass){
            land = "\u001b[48;5;28m";
        }
        else{
            land = "\u001b[48;5;21m";
        }
        box[middle][middle+1] = land + colorWorker + "O";
        box[middle][middle] =  land + colorWorker + "▉";
        box[middle+1][middle] =  land + colorWorker + "╜";
        box[middle][middle-1] = land + colorWorker + "∏";
        box[middle-1][middle] = land + colorWorker + "╙";
    }

    /**
     * set a worker's figure in the box
     * @param color: worker's color
     */
    public void setWorker(PlayerColor color){
        if (level > 0){
            if (color.equals(PlayerColor.YELLOW)) {
                setColorWorker("\u001b[38;5;220m",false);
                playerColor = color;
            }
            else {
                if (color.equals(PlayerColor.BLUE)) {
                    setColorWorker("\u001b[38;5;21m",false);
                    playerColor = color;
                }
                else {
                    if (color.equals(PlayerColor.RED)) {
                        setColorWorker("\u001b[38;5;1m",false);
                        playerColor = color;
                    }
                }
            }
        }
        else {
            if (color.equals(PlayerColor.YELLOW)) {
                setColorWorker("\u001b[38;5;220m",true);
                playerColor = color;
            }
            else {
                if (color.equals(PlayerColor.BLUE)) {
                    setColorWorker("\u001b[38;5;21m",true);
                    playerColor = color;
                }
                else {
                    if (color.equals(PlayerColor.RED)) {
                        setColorWorker("\u001b[38;5;1m",true);
                        playerColor = color;
                    }
                }
            }
        }
        setBorder();
        hasWorker = true;
    }

    /**
     *  print on screen correspondent box's line
     */
    public void printBox(int line){
        for(int j = dimension -1; j >= 0; j--){
                System.out.print(box[j][line]);
        }
    }

    /**
     * set a grey border
     */
    private void setBorder(){
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (x==0 || x==dimension - 1|| y==0 || y==dimension-1) {
                    this.box[x][y]="\u001b[48;5;0m"+"\u001b[38;5;326m"+"▉";
                }
            }
        }
    }

    /**
     *  modify box when building a structure
     */
    public void buildStructure() {
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    /* now color in the darkest grey the level's border and in white others box */
                    if ((level == 0) && (y == 1 || x == 1 || x == dimension - 2 || y == dimension - 2)) {
                        this.box[x][y] = "\u001b[48;5;240m" + "\u001b[38;5;240m" + "▉";
                    } else {
                        if ((level == 1) && (y == 2 || x == 2 || x == dimension - 3 || y == dimension - 3)) {
                            this.box[x][y] = "\u001b[48;5;241m" + "\u001b[38;5;241m" + "▉";
                        } else {
                            if ((level == 2) && (y == 3 || x == 3 || x == dimension - 3 || y == dimension - 3)) {
                                this.box[x][y] = "\u001b[48;5;242m" + "\u001b[38;5;242m" + "▉";
                            } else {
                                this.box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;255m" + "▉";
                            }
                        }
                    }
                }
            }
            //setBorder();
            level++;
            if (hasWorker) {
                setWorker(playerColor);
            }
    }

    public void setLevel(int level) {
        this.level = level;
        setBoxWhitOutWorker();
    }

    /**
     * modify box to a box building a dome
     */
    public void buildDome() {
        for  (int y = 0; y < dimension; y++){
            for (int x = 0; x < dimension; x++) {
                this.box[x][y] = "\u001b[48;5;21m" + "\u001b[38;5;21m" + "▉";

                if (x == 1) {
                    if (y == 1) {
                        box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▜";
                    }
                    if (y == 3) {
                        box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▟";
                    }
                } else {
                    if (x == 3) {
                        if (y == 1) {
                            box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▛";
                        }
                        if (y == 3) {
                            box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▙";
                        }
                    }
                }
            }
        }
        setBorder();
        level++;
    }

    /**
     * set box's color to green
     */
    public void setBoxWhitOutWorker() {
        if (level==0){
            /*set grass*/
            for (int x=0; x<dimension;x++){
                for (int y=0; y<dimension;y++) {
                    if (x != 0 && y != 0) {
                        box[x][y] = "\u001b[48;5;22m"+"\u001b[38;5;28m"+"▉";
                    }
                }
            }
        }
        else {
            this.level=this.level-1;
            buildStructure();
        }
        hasWorker=false;
        playerColor=null;
    }

}