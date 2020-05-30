package it.polimi.ingsw.client.view.gui;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.Worker;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BoxGUI extends JButton  {

    private boolean haveWorker;
    private int x;
    private int y;
    private Worker worker;
    private int level;
    private String grassPath;

    /**
     * @param x: box's abscissa
     * @param y: box's ordinate
     */
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

    /**
     * load in this box the related dome image
     */
    public void buildDome() {
        String domePath = "level/dome.jpg";
        try{
            setIcon(new ImageIcon(ImageIO.read(getClass().getResource(domePath))));
        }catch (IOException ignored){}
        setBackground(new Color(255,255,255));
    }

    /**
     * @return box coordinate
     */
    public ArrayList<Integer> getCoordinates(){
        ArrayList<Integer> returner=new ArrayList<>();
        returner.add(x);
        returner.add(y);
        return returner;
    }

    public int getLevel() {
        return level;
    }

    public Worker getWorker(){
        return worker;
    }

    public boolean getHaveWorker(){
        return haveWorker;
    }

    /**
     * load the original box's image whit out worker and set the construction, in case
     */
    private void setBoxWhitOutWorker() {
        String level2Path = "level/level3.png";
        String level3Path = "level/level2.png";
        String level1Path = "level/level1.png";
        System.out.println(level);
        try {
            switch (level) {
                case 0:
                    setIcon(new ImageIcon(ImageIO.read(getClass().getResource(grassPath))));
                    setBackground(new Color(98, 229, 92));
                    break;
                case 1:

                    setIcon(new ImageIcon(ImageIO.read(getClass().getResource(level1Path))));
                    setBackground(new Color(165, 165, 165));
                    break;
                case 2:
                    setIcon(new ImageIcon(ImageIO.read(getClass().getResource(level2Path))));
                    setBackground(new Color(165, 165, 165));
                    break;
                case 3:
                    setIcon(new ImageIcon(ImageIO.read(getClass().getResource(level3Path))));
                    setBackground(new Color(165, 165, 165));
                    break;
                default:
                    buildDome();
            }
        }catch (IOException ignored){}
    }

    /**
     * set level parameter and build/demolish building in case
     * @param level: box's impose level
     */
    public void setLevel(int level){
        this.level = level;
        setBoxWhitOutWorker();
    }

    /**
     * pose a worker in this box
     * @param worker: worker to pose
     */
    public void setWorker(Worker worker) {
        String colorS,path;
        this.worker=worker;
        haveWorker=true;
        switch (worker.getPlayer().getColor()){
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
            setIcon(new ImageIcon(ImageIO.read(getClass().getResource(path))));
        }catch (IOException ignored) { }
    }



}

