package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerScreen {
    private JFrame jf;
    private JLabel question = new JLabel("Do you want to play on a CLI or on a GUI ?");
    private boolean condition, waitForUpdate;


    ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
    JLabel label = new JLabel(background);


    JButton cli = new JButton("CLI");
    JButton gui = new JButton("GUI");

    public PlayerScreen(JFrame frame){
        jf = frame;
        label.add(cli);
        label.add(gui);
        jf.add(label);
        jf.setVisible(true);
    }

    public void init() {

        label.setBounds(0,0,background.getIconWidth(), background.getIconHeight());

        jf.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = (JPanel) jf.getContentPane();
        jp.setOpaque(false);
        JPanel jpanel = new JPanel();
        jpanel.setOpaque(false);
        jpanel.setLayout(null);

        question.setBounds(150, 120, 400, 30);
        cli.setBounds(180, 215, 110, 40);
        gui.setBounds(350, 215, 110, 40);

        question.setFont(new Font("Times New Roman",Font.ITALIC,20));


        jpanel.add(question);
        jpanel.add(cli);
        jpanel.add(gui);

        jf.add(jpanel);
        jf.setSize(640, 450);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        cli.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                condition = false;
                waitForUpdate=false;
                synchronized (PlayerScreen.this){
                    PlayerScreen.this.notifyAll();
                }
            }
        });
        gui.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                condition = true;
                waitForUpdate=false;
                synchronized (PlayerScreen.this){
                    PlayerScreen.this.notifyAll();
                }
            }
        });
        waitForUpdate=true;
        try{
            while (waitForUpdate){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public boolean getCondition(){
        return condition;
    }
}

