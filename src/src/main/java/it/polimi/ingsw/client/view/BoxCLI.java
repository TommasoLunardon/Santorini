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
     * set a worker's figure in the box
     * @param color: worker's color
     */
    public void setWorker(PlayerColor color){
        int middle = 2;
        if (level > 0){
            if (color.equals(PlayerColor.YELLOW)) {
                box[middle][middle+1] = "\u001b[48;5;21m" + "\u001b[38;5;220m" + "O";
                box[middle][middle] =  "\u001b[48;5;21m" + "\u001b[38;5;220m" + "▉";
                box[middle+1][middle] =  "\u001b[48;5;21m" + "\u001b[38;5;220m" + "╜";
                box[middle][middle-1] = "\u001b[48;5;21m" + "\u001b[38;5;220m" + "∏";
                box[middle-1][middle] = "\u001b[48;5;21m" + "\u001b[38;5;220m" + "╙";
                playerColor = color;
            }
            else {
                if (color.equals(PlayerColor.BLUE)) {
                    box[middle][middle+1] = "\u001b[48;5;21m" + "\u001b[38;5;21m" + "O";
                    box[middle][middle] = "\u001b[48;5;21m" + "\u001b[38;5;21m" + "▉";
                    box[middle+1][middle] = "\u001b[48;5;21m" + "\u001b[38;5;21m" + "╜";
                    box[middle][middle-1] = "\u001b[48;5;21m" + "\u001b[38;5;21m" + "∏";
                    box[middle-1][middle] = "\u001b[48;5;21m" + "\u001b[38;5;21m" + "╙";
                    playerColor = color;
                }
                else {
                    if (color.equals(PlayerColor.RED)) {
                        box[middle][middle + 1] = "\u001b[48;5;21m" + "\u001b[38;5;1m" + "O";
                        box[middle][middle] = "\u001b[48;5;21m" + "\u001b[38;5;1m" + "▉";
                        box[middle + 1][middle] = "\u001b[48;5;21m" + "\u001b[38;5;1m" + "╜";
                        box[middle][middle - 1] = "\u001b[48;5;21m" + "\u001b[38;5;1m" + "∏";
                        box[middle - 1][middle] = "\u001b[48;5;21m" + "\u001b[38;5;1m" + "╙";
                        playerColor = color;
                    }
                }
            }
        }
        else {
            if (color.equals(PlayerColor.YELLOW)) {
                box[middle][middle + 1] = "\u001b[48;5;28m" + "\u001b[38;5;220m" + "O";
                box[middle][middle] = "\u001b[48;5;28m" + "\u001b[38;5;220m" + "▉";
                box[middle + 1][middle] = "\u001b[48;5;28m" + "\u001b[38;5;220m" + "╙";
                box[middle][middle - 1] = "\u001b[48;5;28m" + "\u001b[38;5;220m" + "∏";
                box[middle - 1][middle] = "\u001b[48;5;28m" + "\u001b[38;5;220m" + "╜";
                playerColor = color;
            } else {
                if (color.equals(PlayerColor.BLUE)) {
                    box[middle][middle + 1] = "\u001b[48;5;28m" + "\u001b[38;5;21m" + "O";
                    box[middle][middle] = "\u001b[48;5;28m" + "\u001b[38;5;21m" + "▉";
                    box[middle + 1][middle] = "\u001b[48;5;28m" + "\u001b[38;5;21m" + "╙";
                    box[middle][middle - 1] = "\u001b[48;5;28m" + "\u001b[38;5;21m" + "∏";
                    box[middle - 1][middle] = "\u001b[48;5;28m" + "\u001b[38;5;21m" + "╜";
                    playerColor = color;
                } else {
                    if (color.equals(PlayerColor.RED)) {
                        box[middle][middle + 1] = "\u001b[48;5;28m" + "\u001b[38;5;1m" + "O";
                        box[middle][middle] = "\u001b[48;5;28m" + "\u001b[38;5;1m" + "▉";
                        box[middle + 1][middle] = "\u001b[48;5;28m" + "\u001b[38;5;1m" + "╙";
                        box[middle][middle - 1] = "\u001b[48;5;28m" + "\u001b[38;5;1m" + "∏";
                        box[middle - 1][middle] = "\u001b[48;5;28m" + "\u001b[38;5;1m" + "╜";
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

                if (x == 2) {
                    if (y == 1) {
                        box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▜";
                    }
                    if (y == dimension - 2) {
                        box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▟";
                    }
                } else {
                    if (x == 1) {
                        if (y == 2) {
                            box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▜";
                        }
                        if (y == dimension - 3) {
                            box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▟";
                        }
                    } else {
                        if (x == dimension - level - 3) {
                            if (y == 1) {
                                box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▛";
                            }
                            if (y == dimension - level - 2) {
                                box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▙";
                            }
                        } else {
                            if (x == dimension - 2) {
                                if (y == level + 2) {
                                    box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▛";
                                }
                                if (y == dimension - 3) {
                                    box[x][y] = "\u001b[48;5;255m" + "\u001b[38;5;21m" + "▙";
                                }
                            }
                        }
                    }
                }
            }
        }
        this.box[1][1] = "\u001b[48;5;255m" + "\u001b[38;5;253m" + "▉";
        this.box[dimension - 2][1] = "\u001b[48;5;255m" + "\u001b[38;5;253m" + "▉";
        this.box[1][dimension - 2] = "\u001b[48;5;255m" + "\u001b[38;5;253m" + "▉";
        this.box[dimension - 2][dimension - 2] = "\u001b[48;5;255m" + "\u001b[38;5;253m" + "▉";
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