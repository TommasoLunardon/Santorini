package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMode {
    private JFrame jf = new JFrame("[Game Mode]");
    private JLabel question = new JLabel("Do you want to play with gods? ");
    private JRadioButton yes = new JRadioButton("YES");
    private JRadioButton no = new JRadioButton("NO");

    ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
    JLabel label = new JLabel(background);
    JButton select = new JButton("SELECT");

    public void init() {



        label.setBounds(0,0,background.getIconWidth(), background.getIconHeight());

        jf.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = (JPanel) jf.getContentPane();
        jp.setOpaque(false);
        JPanel jpanel = new JPanel();
        jpanel.setOpaque(false);
        jpanel.setLayout(null);

        question.setFont(new Font("Times New Roman",Font.ITALIC,20));
        question.setBounds(210, 110, 300, 30);
        no.setBounds(340, 172, 90, 23);
        yes.setBounds(260, 172, 90, 23);
        select.setBounds(280, 260, 100, 25);

        ButtonGroup group = new ButtonGroup();

        jpanel.add(question);
        group.add(yes);
        jf.getContentPane().add(yes);

        group.add(no);
        jf.getContentPane().add(no);
        jpanel.add(select);


        jf.add(jpanel);
        jf.setSize(640, 450);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        select.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (yes.isSelected()) {
                    getmode();
                    System.out.println(getmode());
                    jf.dispose();
                }

                if (no.isSelected()) {
                    getmode();
                    jf.dispose();
                }
                else  {
                    select.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(jf, "Please select if you want to play with gods",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            }
        });
    }

    public String getmode(){
        String gamemode = null;
        if(yes.isSelected()){
            gamemode = "yes";
        }
        if(no.isSelected()){
            gamemode = "no";
        }
        return gamemode;
    }


}
