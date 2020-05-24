package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class User {

    private Game game;

    private JFrame jf = new JFrame("[Players]");
    private JLabel question = new JLabel("Select the starter, IDs available are:");
    private JComboBox avaibleID = new JComboBox();

    ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
    JLabel label = new JLabel(background);
    JButton select = new JButton("SELECT");

    public void init() {

        game.getNumPlayers();

        String[] avaibleids = new String[3];

        for(int i=1;i<game.getNumPlayers();i++){
            avaibleids[0]="";
            avaibleids[i]= game.getIDs().get(i);
        }

        for (String item : avaibleids) {
                avaibleID.addItem(item);
        }

        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());

        jf.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = (JPanel) jf.getContentPane();
        jp.setOpaque(false);
        JPanel jpanel = new JPanel();
        jpanel.setOpaque(false);
        jpanel.setLayout(null);

        question.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        question.setBounds(180, 105, 300, 30);
        select.setBounds(280, 260, 100, 25);
        avaibleID.setBounds(240, 160, 180, 23);

        ButtonGroup group = new ButtonGroup();

        jpanel.add(question);
        jf.getContentPane().add(avaibleID);

        jpanel.add(select);


        jf.add(jpanel);
        jf.setSize(640, 450);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        select.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (avaibleID.getItemAt(avaibleID.getSelectedIndex())!="") {
                    getplayerid();
                    jf.dispose();
                }
                else  {
                    select.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(jf, "Please select a player ID",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            }
        });
    }

    public String getplayerid(){

        return (String) avaibleID.getItemAt(avaibleID.getSelectedIndex());

    }
}
