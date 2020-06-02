package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerNum {
    private JFrame jf;
    private JLabel playernum = new JLabel("Please select the number of Players: ");
    private JRadioButton two = new JRadioButton("2");
    private JRadioButton three = new JRadioButton("3");

    ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
    JLabel label = new JLabel(background);
    JButton select = new JButton("SELECT");
    private boolean waitForUpdate,waitForUpdate2;

    public PlayerNum(JFrame frame){
        jf = frame;
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


        playernum.setBounds(210, 110, 300, 30);
        playernum.setFont(new Font("Times New Roman",Font.ITALIC,18));
        three.setBounds(350, 172, 90, 23);
        two.setBounds(270, 172, 90, 23);
        select.setBounds(280, 260, 100, 25);

        ButtonGroup group = new ButtonGroup();

        jpanel.add(playernum);
        group.add(two);
        jf.getContentPane().add(two);

        group.add(three);
        jf.getContentPane().add(three);
        jpanel.add(select);


        jf.add(jpanel);
        jf.setSize(640, 450);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        select.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (two.isSelected()) {
                    getnum();
                    jf.dispose();
                    waitForUpdate2 = false;
                    synchronized (PlayerNum.this) {
                        PlayerNum.this.notifyAll();
                    }
                } else {
                    if (three.isSelected()) {
                        getnum();
                        jf.dispose();
                        waitForUpdate2 = false;
                        synchronized (PlayerNum.this) {
                            PlayerNum.this.notifyAll();
                        }
                    } else {
                        select.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JOptionPane.showMessageDialog(jf, "Please select the number of players",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                waitForUpdate2 = false;
                                synchronized (PlayerNum.this) {
                                    PlayerNum.this.notifyAll();
                                }
                            }

                        });
                    }
                }
            }
        });
        waitForUpdate2=true;
        while (waitForUpdate2){
            try{
                wait();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }}
        waitForUpdate=true;
        try{
            while (waitForUpdate){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getnum(){
        int num = 0;
        if(two.isSelected()){
            num = 2;
        }
        if(three.isSelected()){
            num = 3;
        }
        return num;
    }



}
