package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MapGUI extends JPanel{
    private BoxGUI[][] map;
    private BoxGUI lastClickedBox;
    private boolean waitForUpdating;
    private ArrayList<Integer> infos;
    private LeftMenu leftMenu;

    public MapGUI() {
        super();
        leftMenu=new LeftMenu(this);
        map= new BoxGUI[5][5];
        setLayout(new GridLayout(5,5));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                BoxGUI boxGUI=new BoxGUI(j,i);
                add(boxGUI);
                map[i][j]=boxGUI;
                map[i][j].addMouseListener(new StopReception());
            }
        }
    }

    public ArrayList<Integer> selectBox(){
        boxPreparer();
        waitForUpdating();
        disableBox();
        return infos;
    }

    public BoxGUI getBox(int coordinateX, int coordinateY){
        return map[coordinateX][coordinateY];
    }

    public BoxGUI getCLickedBox(){
        boxPreparer();
        waitForUpdating();
        disableBox();
        return lastClickedBox;
    }

    public void updateMap(Game game) {
        try
        {for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (game.getMap().getBox(i,j).getLevel()!=map[i][j].getLevel()){
                    map[i][j].setLevel(game.getMap().getBox(i,j).getLevel());
                }
                if ((game.getMap().getBox(i,j).hasWorker()&&map[i][j].getHaveWorker())){
                    map[i][j].setWorker(game.getMap().getBox(i,j).getWorker());
                }
            }
        }
        }catch (InvalidIndicesException | WorkerNotExistException ignore){}

    }

    public LeftMenu getLeftMenu() {
        return leftMenu;
    }

    synchronized private void boxPreparer(){
        for (int i=0; i<5; i++){
            for (int j=0;j<5;j++){
                map[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        BoxGUI box=(BoxGUI) e.getComponent();
                        infos=box.getCoordinates();
                        lastClickedBox=box;
                        waitForUpdating=false;
                        synchronized (MapGUI.this){
                            MapGUI.this.notifyAll();;
                        }
                    }
                });
            }
        }
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

}


class StopReception extends MouseAdapter{
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }
}