package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerAgeColor {

    String communicationString;

    private JFrame jf = new JFrame("[Age and Color]");
    private JLabel selplayerage = new JLabel("Select your age:  ");
    private JLabel selplayercolor = new JLabel("Select a color:  ");
    private JLabel avaiblecolor = new JLabel("color available are:");
    private JLabel avaiblecolor1= new JLabel((String) communicationString);
    private JComboBox agelist = new JComboBox();
    private JComboBox color = new JComboBox();
    private boolean waitForUpdate, waitForUpdate2;

    private void waitForUpdate(){
        waitForUpdate=true;
        while (waitForUpdate){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForUpdate2(){
        waitForUpdate2=true;
        while (waitForUpdate2){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
    JLabel label = new JLabel(background);
    JButton select = new JButton("SELECT");

    public void init() {

        String[] agenum = new String[120];
        for(int i=1;i<120;i++){
            agenum[0]="";
            agenum[i]= String.valueOf(i);
        }
        for (String item : agenum)
        {
            agelist.addItem(item);
        }


        String[] colortype = new String[]{"", "Yellow", "Blue", "Red"};
        for (String item : colortype)
        {
            color.addItem(item);
        }



        label.setBounds(0,0,background.getIconWidth(), background.getIconHeight());

        jf.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = (JPanel) jf.getContentPane();
        jp.setOpaque(false);
        JPanel jpanel = new JPanel();
        jpanel.setOpaque(false);
        jpanel.setLayout(null);

        select.setBounds(280, 260, 100, 25);

        selplayerage.setBounds(200, 110, 140, 30);
        agelist.setBounds(330, 114, 100, 23);

        selplayercolor.setBounds(200, 170, 140, 30);
        color.setBounds(330, 174, 100, 23);
        avaiblecolor.setBounds(300,200,180,23);
        avaiblecolor1.setBounds(430,200,100,23);


        jpanel.add(selplayerage);
        jf.getContentPane().add(agelist);
        jpanel.add(selplayercolor);
        jf.getContentPane().add(color);
        jpanel.add(select);
        jpanel.add(avaiblecolor);
        jpanel.add(avaiblecolor1);


        jf.add(jpanel);
        jf.setSize(640, 450);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        select.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (agelist.getSelectedItem()!="" && color.getSelectedItem()!="") {
                    ret();
                    jf.dispose();
                    waitForUpdate=false;
                    synchronized (PlayerAgeColor.this){
                        PlayerAgeColor.this.notifyAll();;
                    }
                }
                else  {
                    select.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(jf, "Please select your age and the color",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            waitForUpdate2=false;
                            synchronized (PlayerAgeColor.this){
                                PlayerAgeColor.this.notifyAll();;
                            }
                        }
                    });
                    waitForUpdate2();
                }
            }
        });
        waitForUpdate();
    }

    public int getAge(){
       return agelist.getSelectedIndex();
    }
    public String getColor() {
        return (String) color.getItemAt(color.getSelectedIndex());
    }
    public String ret(){
        return getAge()+getColor();
    }


}
