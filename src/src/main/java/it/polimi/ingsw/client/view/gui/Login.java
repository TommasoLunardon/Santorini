package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Login {
  private JFrame jf = new JFrame("[Welcome to Santorini]");
  private JLabel lb1 = new JLabel("USERNAME:");
  private JLabel lb2 = new JLabel("P O R T     :");
  private JLabel uniq = new JLabel("(Username must be unique)");

  private boolean waitForUpdate;
  private boolean waitForUpdate2;

  ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
  JLabel label = new JLabel(background);

  protected JTextField username = new JTextField(15);
  protected JTextField port = new JTextField(15);
  JButton play = new JButton("PLAY");

  public void init() {



    label.setBounds(0,0,background.getIconWidth(), background.getIconHeight());

    jf.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel jp = (JPanel) jf.getContentPane();
    jp.setOpaque(false);
    JPanel jpanel = new JPanel();
    jpanel.setOpaque(false);
    jpanel.setLayout(null);

    lb1.setBounds(200, 110, 140, 30);
    lb2.setBounds(200, 170, 140, 30);
    username.setBounds(280, 112, 180, 23);
    uniq.setBounds(285,135,180,23);
    port.setBounds(280, 172, 180, 23);
    play.setBounds(280, 260, 100, 25);

    jpanel.add(uniq);
    jpanel.add(lb1);
    jpanel.add(lb2);
    jpanel.add(play);
    jpanel.add(username);
    jpanel.add(port);

    jf.add(jpanel);
    jf.setSize(640, 450);
    jf.setVisible(true);
    jf.setResizable(false);
    jf.setLocationRelativeTo(null);


    play.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent evt)
      {
        try{
          Integer.parseInt(port.getText());
          if(!username.getText().equals("")&&"123456".equalsIgnoreCase(port.getText())) {
              getUsername();
              jf.dispose();
          }
        }catch (NumberFormatException e) {
          play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf, "Please enter your username and the right Port",
                        "Error", JOptionPane.ERROR_MESSAGE);
              waitForUpdate2=false;
              synchronized (Login.this){
                Login.this.notifyAll();
              }
              }
            });
        }
        waitForUpdate2=true;
        while (waitForUpdate2){
          try {
            wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        waitForUpdate=false;
        synchronized (Login.this){
          Login.this.notifyAll();
        }
      }
    });
    waitForUpdate=true;
    while (waitForUpdate){
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  public String getUsername(){
    return username.getText();
  }




}

