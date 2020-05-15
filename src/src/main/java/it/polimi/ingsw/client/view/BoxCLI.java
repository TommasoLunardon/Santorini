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


    public int getLevel(){
        return level;
    }

    /**
     * Creation of BoxCLI
     * @param coordinateX: box's coordinates X
     * @param coordinateY: box's coordinates Y
     */
    public BoxCLI(int coordinateX, int coordinateY) {
        this.box = new String[dimension][dimension];
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                box[x][y] = "\u001b[48;5;22m" + "\u001b[38;5;28m" + "▉\t";
            }
        }
        setBorder();
    }

 /*   /**
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
            land="\u001b[48;5;243m";
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    box[x][y] = land + "\u001b[48;5;243m" + "▉\t";
                }
            }
        }
        box[middle][middle-1] = land + colorWorker + " O\t";
        box[middle][middle] =  land + colorWorker + "█";
        box[middle+1][middle] =  land + colorWorker + "╜\t\t";
        box[middle][middle+1] = land + colorWorker + " ∏\t";
        box[middle-1][middle] = land + colorWorker + "\t╙";
        setBorder();
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
        for(int j = 0; j<dimension; j++){
            System.out.print(box[j][line]+"\u001b[0m");
        }
    }

    /**
     * set a grey border
     */
    private void setBorder(){
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (x==0 || x==dimension - 1|| y==0 || y==dimension-1) {
                    box[x][y]="\u001b[48;5;8m"+"\u001b[38;5;8m"+"▉\t";
                }
            }
        }
    }

    /**
     *  modify box when building a structure
     */
    public void buildStructure() {
        String label="\u001b[48;5;243m";
        String place= label+ label + "▉\t";
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                box[x][y]="\u001b[48;5;248m" + "\u001b[48;5;249m" + "▉\t";
            }
        }
        if ((level == 0)) {
            box[1][1]=box[2][1]=box[3][1]=place;
            box[1][2]=box[3][2]=place;
            box[1][3]=box[1][3]=box[1][2]=place;
        } else {
            if ((level == 1)) {
                box[2][1]=box[1][2]=box[3][2]=box[2][3]=place;
            } else {
                if ((level != 2)) {
                    buildDome();
                }
            }
        }
        setBorder();
        level++;
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
                box[x][y]="\u001b[48;5;21m" + "\u001b[38;5;21m"+"▉\t";
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