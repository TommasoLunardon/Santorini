package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.Player;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class LeftMenu extends JLabel {
    private String choose;
    private boolean waitForUpdating;
    private MapGUI mapGUI;

    public LeftMenu(MapGUI mapGUI) {
        super();
        this.mapGUI=mapGUI;
    }

    /**
     * wait until a box is pressed
     */
    synchronized private void waitForUpdating() {
        waitForUpdating=true;
        while (waitForUpdating) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
    }


    /**
     * for special power, exit, game over
     * @param nameButtonTrue: text in button for true condition
     * @param nameButtonFalse: text in button for false condition
     * @param text: message for user
     * @return user answer <yes/no>
     */
    public String askCondition(String nameButtonTrue,String nameButtonFalse, String text){
        JButton yes=new JButton(nameButtonTrue);
        yes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                choose= "yes";
                waitForUpdating=false;
                synchronized (LeftMenu.this) {
                    LeftMenu.this.notifyAll();
                }
            }
        });

        JButton no=new JButton(nameButtonFalse);
        no.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                choose="no";
                waitForUpdating=false;
                synchronized (LeftMenu.this) {
                    LeftMenu.this.notifyAll();
                }
            }
        });
        setText(text);
        waitForUpdating();
        removeAll();
        return choose;
    }

    /**
     * @return position of worker in player array list
     */
    public int askForWorker(Player player){
        BoxGUI box;
        int ret;
        do {
            setText("Select a worker from map");
            box=mapGUI.getCLickedBox();
        }while (!(box.getHaveWorker()&&player.getWorkers().contains(box.getWorker())));
        removeAll();
        if (player.getWorkers().get(0).equals(box.getWorker())){
            ret=0;
        }
        else ret=1;
        return ret;
    }

}


