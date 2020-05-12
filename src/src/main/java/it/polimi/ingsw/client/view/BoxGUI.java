package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.PlayerColor;

import javax.swing.*;
import java.awt.*;

public class BoxGUI extends JButton  implements Box{
    private Image icon;
    private int coordinateX, coordinateY;
    private int level;
    public BoxGUI (int coordinateX, int coordinateY){
        super();
        setOpaque(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        this.coordinateX=coordinateX;
        this.coordinateY=coordinateY;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String[][] getBox() {
        return new String[0][];
    }

    @Override
    public void setWorker(PlayerColor color) throws InputFailedException {

    }

    @Override
    public void printBox(int line) {

    }

    @Override
    public void buildStructure() {

    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public void buildDome() {

    }

    @Override
    public void setBoxWhitOutWorker() {

    }
}
