package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.Player;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class LeftMenu extends JLabel {
    private boolean choose;
    private boolean waitForUpdating;
    private MapGUI mapGUI;

    public LeftMenu(MapGUI mapGUI) {
        super();
        this.mapGUI=mapGUI;
    }

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
     * @return user answer
     */
    public boolean askCondition(String nameButtonTrue,String nameButtonFalse, String text){
        JButton yes=new JButton(nameButtonTrue);
        yes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                choose=waitForUpdating=false;
            }
        });

        JButton no=new JButton(nameButtonFalse);
        no.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                choose=waitForUpdating=false;
            }
        });
        setText(text);

        Thread waitUpadate = new Thread() {
            @Override
            public void run() {
                super.run();
                synchronized (LeftMenu.this){
                    waitForUpdating();
                }
            }
        };
        waitUpadate.start();
        return choose;
    }

    /**
     * @return position of worker in player array list
     */
    public int askForWorker(Player player){
        BoxGUI box;
        int ret;
        do {
            setText("Select a worker");
            box=mapGUI.getCLickedBox();
        }while (!(box.haveWorker()&&player.getWorkers().contains(box.getWorker())));
        setText("");
        if (player.getWorkers().get(0).equals(box.getWorker())){
            ret=0;
        }
        else ret=1;

        return ret;

    }



/*    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
*/
}


