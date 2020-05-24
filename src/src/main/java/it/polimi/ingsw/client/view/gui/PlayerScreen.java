package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerScreen {
    private JFrame jf = new JFrame("[Welcome to Santorini]");
    private JLabel question = new JLabel("Do you want to play on a CLI or on a GUI ?");


    ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
    JLabel label = new JLabel(background);


    JButton cli = new JButton("CLI");
    JButton gui = new JButton("GUI");

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
                cli();
                System.out.println(cli());
                jf.dispose();
            }
        });
        gui.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                gui();
                jf.dispose();
            }
        });
    }

    public boolean cli(){
        return false;
    }
    public boolean gui(){
        return true;
    }



}

