package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MapGUI extends JPanel {
    private BoxGUI[][] map;
    private BoxGUI lastClickedBox;
    private boolean waitForUpdating;
    private ArrayList<Integer> infos;

    public MapGUI() {
        super();
        map= new BoxGUI[5][5];
        setLayout(new GridLayout(5,5));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                BoxGUI boxGUI=new BoxGUI(j,i);
                add(boxGUI);
                map[i][j]=boxGUI;
            }
        }
    }

    synchronized private void boxPreparer(){
        MapGUI mapGUIo=this;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                 map[i][j].addMouseListener(new MouseAdapter() {
                     private MapGUI mapGUI=mapGUIo;
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (e.getComponent() instanceof  BoxGUI){
                            lastClickedBox=(BoxGUI) e.getComponent();
                            infos=lastClickedBox.coordinates();
                           // System.out.println("i listened"+infos.get(0)+infos.get(1));
/*------------------>>>>>>>>>>>>>> chiedere aiuto*/                            mapGUI.notifyAll();
                        }
                    }
                });
            }
        }
        waitForUpdating=true;
    }

    public void updateMap(Game game, Player player){
        try
        {for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (game.getMap().getBox(i,j).getLevel()!=map[i][j].getLevel()){
                    map[i][j].setLevel(game.getMap().getBox(i,j).getLevel());
                }
                if ((game.getMap().getBox(i,j).hasWorker()&&map[i][j].haveWorker())){
                    map[i][j].setWorker(player.getColor(),game.getMap().getBox(i,j).getWorker());
                }
            }
        }
        }catch (InvalidIndicesException | WorkerNotExistException ignore){}

    }

    public BoxGUI getCLickedBox(){
        boxPreparer();
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                synchronized (MapGUI.this){
                    waitForUpdating();
                }
            }
        };
        thread.start();
        disableBox();
        return lastClickedBox;
    }

    private void disableBox(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                map[i][j].addMouseListener(new StopReception());
            }
        }
    }

    synchronized private void waitForUpdating(){
            while (waitForUpdating) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
    }

    public ArrayList<Integer> selectBox(){
        ArrayList<Integer> coordinates;
        boxPreparer();
        Thread thread = new Thread() {
           @Override
           public void run() {
               super.run();
               synchronized (MapGUI.this){
                   waitForUpdating();
               }
           }
       };
        thread.start();
        disableBox();
        coordinates=infos;
        return coordinates;
    }

    public BoxGUI getBox(int coordinateX, int coordinateY){
        return map[coordinateX][coordinateY];
    }
}


class StopReception extends MouseAdapter{
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }
}