package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.PlayerColor;

import javax.swing.*;
import java.awt.*;

public class BoxGUI extends JButton {
    private Image icon;
    private static int dimension = 5;
    private int level;
    private boolean isMale;
    private boolean hasWorker;
    private PlayerColor playerColor;
    private JPanel mapGUI;
    public BoxGUI (JPanel map){
        super();
        this.setSize(50,50); //box's dimentions
        mapGUI=map;
    }

    public int getLevel() {
        return level;
    }

    public void setWorker(PlayerColor color){

        if (color.equals(PlayerColor.YELLOW)){
           setForeground( new ImageIcon("ing-sw-2020-Gatti-Huang-Lunardon/ProgettoIngSW/src/src/mainresource/FemaleBuilder_Orange_v001.png"));
        }else {
            if (color.equals(PlayerColor.BLUE)) {
                setIcon(new ImageIcon("ing-sw-2020-Gatti-Huang-Lunardon/ProgettoIngSW/src/src/mainresource/FemaleBuilder_Tan_v001.png"));
            } else
                {
                if (color.equals(PlayerColor.RED)) {
                    setIcon(new ImageIcon("ing-sw-2020-Gatti-Huang-Lunardon/ProgettoIngSW/src/src/mainresource/FemaleBuilder_Purple_v001.png"));
                }
            }
        }

    }


    public void printBox(int line) {

    }

    public void buildStructure() {

    }

    public void setLevel(int level) {
        this.level=level;
    }

    public void buildDome() {

    }

    public void setBoxWhitOutWorker() {

    }
}
