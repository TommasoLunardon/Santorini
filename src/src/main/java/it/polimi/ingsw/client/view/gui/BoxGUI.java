package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.PlayerColor;
import it.polimi.ingsw.server.model.Worker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BoxGUI extends JButton{

    private boolean haveWorker;
    private int x;
    private int y;
    private Worker worker;
    private int level;
    private String grassPath;


    public Worker getWorker(){
        return worker;
    }

    public BoxGUI(int x, int y){
        try {
            this.x=x;
            this.y=y;
            grassPath="SingleBox/bo"+x+y+".png";
            setIcon(new ImageIcon(ImageIO.read(new File(String.valueOf(getClass().getResource(grassPath))))));
            setBackground(new Color(98, 229, 92));
            setOpaque(true);
            setVisible(true);
            level=0;
            setBoxWhitOutWorker();
            setContentAreaFilled(true);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public int getLevel() {
        return level;
    }

    public void setWorker(PlayerColor color, Worker worker) {
        String colorS,path;
        haveWorker=true;
        switch (color){
            case YELLOW: colorS="Yellow";break;
            case BLUE: colorS="Blue";break;
            default:colorS="Red";
        }
        if (level==0){
            path="boxWhitWorkers/level0/"+colorS+"bo"+x+y+".png";
            setBackground(new Color(98, 229, 92));
        }else{
            path="boxWhitWorkers/level"+level+"/"+colorS+"/level"+level+".png";
            setBackground(new Color(165, 165, 165));
        }
        try {
            setIcon(new ImageIcon(ImageIO.read(new FileInputStream(String.valueOf(getClass().getResource(path))))));
        }catch (IOException ignored) { }
        this.worker=worker;
    }

    public void setLevel(int level){
        this.level = level;
        try {
            setBoxWhitOutWorker();
        }catch (IOException ignore){}
    }

    public boolean haveWorker(){
        return haveWorker;
    }

    public void buildDome() throws IOException {
        String domePath = "level/dome.jpg";
        setIcon(new ImageIcon(ImageIO.read(new File(String.valueOf(getClass().getResource(domePath))))));
        setBackground(new Color(255,255,255));
    }

    private void setBoxWhitOutWorker() throws IOException {
        String level2Path = "level/level3.png";
        String level3Path = "level/level2.png";
        String level1Path = "level/level1.png";
        System.out.println(level);
        switch (level) {
            case 0:
                setIcon(new ImageIcon(ImageIO.read(new File(String.valueOf(getClass().getResource(grassPath))))));
                setBackground(new Color(98, 229, 92));break;
            case 1:

                setIcon(new ImageIcon(ImageIO.read(new File(String.valueOf(getClass().getResource(level1Path))))));
                setBackground(new Color(165, 165, 165));break;
            case 2:
                setIcon(new ImageIcon(ImageIO.read(new File(String.valueOf(getClass().getResource(level2Path))))));
                setBackground(new Color(165, 165, 165)); break;
            case 3:
                setIcon(new ImageIcon(ImageIO.read(new File(String.valueOf(getClass().getResource(level3Path))))));
                setBackground(new Color(165, 165, 165));break;
            default:

                buildDome();
        }
    }

    public ArrayList<Integer> coordinates(){
        ArrayList<Integer> returner=new ArrayList<>();
        returner.add(x);
        returner.add(y);
        return returner;
    }
}

