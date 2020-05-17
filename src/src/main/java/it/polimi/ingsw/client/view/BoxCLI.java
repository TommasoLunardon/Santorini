package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.PlayerColor;

/**
 * Class used to control the specific visual unit box
 * @author Gabriele Gatti
 */

public class BoxCLI {
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

    private void setBorder(){
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (x==0 || x==dimension - 1|| y==0 || y==dimension-1) {
                    box[x][y]="\u001b[48;5;8m"+"\u001b[38;5;8m"+"▉\t";
                }
            }
        }
    }

    public void buildDome() {
        for  (int y = 0; y < dimension; y++){
            for (int x = 0; x < dimension; x++) {
                box[x][y]="\u001b[48;5;21m" + "\u001b[38;5;21m"+"▉\t";
            }
        }
        setBorder();
    }

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

    public void plantGrass(){
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                box[x][y] = "\u001b[48;5;22m" + "\u001b[38;5;28m" + "▉\t";
            }
        }
        setBorder();
    }

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

    public void setWorker(PlayerColor color){
        setWorker(catchColor(color));
    }

    public void setWorker(String colorWorker){
        boolean condition;
        condition= level == 0;
        setColorWorker(colorWorker,condition);
    }

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

    public void printBox(int line){
        for(int j = 0; j<dimension; j++){
            System.out.print(box[j][line]+"\u001b[0m");
        }
    }

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