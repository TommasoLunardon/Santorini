package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.PlayerColor;

/**
 * Class used to represent a map's box for the client
 */

public class BoxCLI extends Box {
    private int dimension;
    private  String[][] box;
    private  int level;
    private boolean hasWorker;

    public int getLevel() { return level;}

    public BoxCLI() {
        dimension=5;
        box = new String[5][5];
        plantGrass();
    }

    /**
     * Method used to graphically represent the box's border
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
     * Method used to graphically represent a dome on the box
     */
    public void buildDome() {
        for  (int y = 0; y < dimension; y++){
            for (int x = 0; x < dimension; x++) {
                box[x][y]="\u001b[48;5;21m" + "\u001b[38;5;21m"+"▉\t";
            }
        }
        setBorder();
    }
    /**
     * Method used to graphically represent the blocks on the box
     */
    public void buildStructure() {
        String label="\u001b[48;5;249m";
        String place= label+ "\u001b[38;5;249m" + "▉\t";
        if (level < 4){
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    box[x][y] = place;
                }
            }
            box[1][1]="\u001b[48;5;249m" + "\u001b[38;5;255m" + level + "\t";
            //level++;

        }
        else buildDome();
        setBorder();
    }

    /**
     * Method used to represent the box at level 0
     */
    public void plantGrass(){
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                box[x][y] = "\u001b[48;5;22m" + "\u001b[38;5;28m" + "▉\t";
            }
        }
        setBorder();
    }

    /**
     * Method used to represent a box freed by its worker
     */
    public void leaveWorker(){
        if (level==0){
            plantGrass();
        }
        else {
            level--;
            buildStructure();
        }
        setBorder();
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
     * @param color worker's color
     */
    public void setWorker(PlayerColor color){
        setWorker(catchColor(color));
    }
    /**
     * Method used to set a worker in the box (x,y)
     * @param colorWorker worker's color
     */
    public void setWorker(String colorWorker){
        boolean condition;
        condition= level == 0;
        setColorWorker(colorWorker,condition);
    }

    /**
     * Method used to graphically set a worker's color
     * @param colorWorker is the color of the worker
     * @param onGrass is true <==> the box has level = 0
     */
    private void setColorWorker(String colorWorker, boolean onGrass) {
        int middle = dimension/2;
        String land;
        if (onGrass){
            land = "\u001b[48;5;28m";
        }
        else{
            land="\u001b[48;5;249m";
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    box[x][y] = land + "\u001b[38;5;249m" + "▉\t";
                }
            }
            box[1][1]="\u001b[48;5;249m" + "\u001b[38;5;255m" + level + "\t";
        }
        box[middle][middle-1] = land + colorWorker + " O\t";
        box[middle][middle] =  land + colorWorker + "█";
        box[middle+1][middle] =  land + colorWorker + "╜\t\t";
        box[middle][middle+1] = land + colorWorker + " ∏\t";
        box[middle-1][middle] = land + colorWorker + "\t╙";
        setBorder();
    }

    /**
     * Method used to graphically represent the box on the screen
     * @param line
     */
    public void printBox(int line){
        for(int j = 0; j<dimension; j++){
            System.out.print(box[j][line]+"\u001b[0m");
        }
    }

    /**
     * Method used to graphically represent a box at a given level
     * @param level is the box's level
     */
    public void setLevel(int level) {
        this.level = level;
        if(level == 0){
            plantGrass();
        }
        else{
            buildStructure();
        }
    }
}