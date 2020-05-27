package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * this class represent the selection of two or three divinity
 * @author Jing Huang
 */

public class DivinityT {
    String communicationString;

    private JFrame jf = new JFrame("[Divinity cards]");
    private JCheckBox apolloname = new JCheckBox("Apollo");
    private JCheckBox artemisname = new JCheckBox("Artemis");
    private JCheckBox atenaname = new JCheckBox("Athena");
    private JCheckBox atlasname = new JCheckBox("Atlas");
    private JCheckBox demetername = new JCheckBox("Demeter");
    private JCheckBox hephaestusname = new JCheckBox("Hephaestus");
    private JCheckBox humanname = new JCheckBox("Human");
    private JCheckBox minotaurname = new JCheckBox("Minotaur");
    private JCheckBox panname = new JCheckBox("Pan");
    private JCheckBox prometheusname = new JCheckBox("Prometheus");


    private JLabel title = new JLabel("Select the cards");
    private JLabel avaibledivinity = new JLabel("Gods available are:");
    private JLabel avaibledivinity1= new JLabel((String) communicationString);

    int width = 85;
    int height = 122;


    ImageIcon background = new ImageIcon("/src/main/resources/bg_panelMid.png");
    ImageIcon apollo = new ImageIcon("/src/main/resources/Divinity/apollo.png");
    ImageIcon artemis = new ImageIcon("/src/main/resources/Divinity/Artemis.png");
    ImageIcon athena = new ImageIcon("/src/main/resources/Divinity/Athena.png");
    ImageIcon atlas = new ImageIcon("/src/main/resources/Divinity/Atlas.png");
    ImageIcon demeter = new ImageIcon("/src/main/resources/Divinity/Demeter.png");
    ImageIcon hephaestus = new ImageIcon("/src/main/resources/Divinity/Hephaestus.png");
    ImageIcon minotaur = new ImageIcon("/src/main/resources/Divinity/Minotaur.png");
    ImageIcon pan = new ImageIcon("/src/main/resources/Divinity/Pan.png");
    ImageIcon prometheus = new ImageIcon("/src/main/resources/Divinity/Prometheus.png");
    ImageIcon human = new ImageIcon("/src/main/resources/Divinity/Human.png");

    JLabel label = new JLabel(background);
    JButton select = new JButton("SELECT");

    JButton apolloButton = new JButton(apollo);
    JButton artemisButton = new JButton(artemis);
    JButton athenaButton = new JButton(athena);
    JButton atlasButton = new JButton(atlas);
    JButton demeterButton = new JButton(demeter);
    JButton hephaestusButton = new JButton(hephaestus);
    JButton minotaurButton = new JButton(minotaur);
    JButton panButton = new JButton(pan);
    JButton prometheusButton = new JButton(prometheus);
    JButton humanButton = new JButton(human);


    public void init(int playernum) {

        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        apollo.setImage(apollo.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        artemis.setImage(artemis.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        athena.setImage(athena.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        atlas.setImage(atlas.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        demeter.setImage(demeter.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        hephaestus.setImage(hephaestus.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        minotaur.setImage(minotaur.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        pan.setImage(pan.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        prometheus.setImage(prometheus.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        human.setImage(human.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));


        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));
        JPanel jp = (JPanel) jf.getContentPane();
        jp.setOpaque(false);
        JPanel jpanel = new JPanel();
        jpanel.setOpaque(false);
        jpanel.setLayout(null);


        select.setBounds(270, 380, 100, 25);

        title.setBounds(255,18,400,25);
        title.setFont(new Font("Times New Roman",Font.ITALIC,18));

        avaibledivinity.setBounds(40,355,180,25);
        avaibledivinity.setFont(new Font("Times New Roman",Font.ITALIC,16));
        avaibledivinity1.setBounds(170,355,200,25);

        humanname.setBounds(30, 45, 110, 25);
        humanButton.setBounds(40, 69, 85, 122);

        apolloname.setBounds(145, 45, 110, 25);
        apolloButton.setBounds(155, 69, 85, 122);

        artemisname.setBounds(260, 45, 110, 25);
        artemisButton.setBounds(270, 69, 85, 122);

        atenaname.setBounds(375, 45, 110, 25);
        athenaButton.setBounds(385, 69, 85, 122);

        atlasname.setBounds(490, 45, 110, 25);
        atlasButton.setBounds(500, 69, 85, 122);

        demetername.setBounds(30, 200, 110, 25);
        demeterButton.setBounds(40, 224, 85, 122);

        hephaestusname.setBounds(145, 200, 110, 25);
        hephaestusButton.setBounds(155, 224, 85, 122);

        minotaurname.setBounds(260, 200, 110, 25);
        minotaurButton.setBounds(270, 224, 85, 122);

        panname.setBounds(375, 200, 110, 25);
        panButton.setBounds(385, 224, 85, 122);

        prometheusname.setBounds(490, 200, 110, 25);
        prometheusButton.setBounds(500, 224, 85, 122);


        it.polimi.ingsw.client.view.gui.CheckDivinityNum group = new it.polimi.ingsw.client.view.gui.CheckDivinityNum(playernum);

        jpanel.add(select);
        jpanel.add(title);

        group.register(apolloname);
        jf.getContentPane().add(apolloname);
        jpanel.add(apolloButton);


        group.register(artemisname);
        jf.getContentPane().add(artemisname);
        jpanel.add(artemisButton);

        group.register(atenaname);
        jf.getContentPane().add(atenaname);
        jpanel.add(athenaButton);

        group.register(atlasname);
        jf.getContentPane().add(atlasname);
        jpanel.add(atlasButton);

        group.register(demetername);
        jf.getContentPane().add(demetername);
        jpanel.add(demeterButton);

        group.register(hephaestusname);
        jf.getContentPane().add(hephaestusname);
        jpanel.add(hephaestusButton);

        group.register(minotaurname);
        jf.getContentPane().add(minotaurname);
        jpanel.add(minotaurButton);

        group.register(panname);
        jf.getContentPane().add(panname);
        jpanel.add(panButton);

        group.register(prometheusname);
        jf.getContentPane().add(prometheusname);
        jpanel.add(prometheusButton);

        group.register(humanname);
        jf.getContentPane().add(humanname);
        jpanel.add(humanButton);

        jpanel.add(avaibledivinity);
        jpanel.add(avaibledivinity1);


        jf.add(jpanel);
        jf.setSize(640, 450);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        JCheckBox[] boxArray = new JCheckBox[]{apolloname, artemisname, atenaname,atlasname,demetername,hephaestusname,humanname,minotaurname,panname,prometheusname};

        select.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                getSelectedDivinity(boxArray);
                jf.dispose();
            }
        });

        apolloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to" +
                        "the space yours just vacated.","God Of Music",JOptionPane.PLAIN_MESSAGE);
            }
        });
        artemisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Your Move: Your Worker may move one additional time, but not back to its initial space.","Goddess of the Hunt",JOptionPane.PLAIN_MESSAGE);
            }
        });
        athenaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot\n" +
                        "move up this turn.","Goddess of Wisdom",JOptionPane.PLAIN_MESSAGE);
            }
        });
        atlasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Your Build: Your Worker may build a dome at any level.","Titan Shouldering the Heavens",JOptionPane.PLAIN_MESSAGE);
            }
        });
        demeterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Your Build: Your Worker may build one additional time, but not on the same space.","Goddess of the Harvest",JOptionPane.PLAIN_MESSAGE);
            }
        });
        hephaestusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Your Build: Your Worker may build one additional block (not dome) on top of your first block.","God of Blacksmiths",JOptionPane.PLAIN_MESSAGE);
            }
        });
        minotaurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be\n" +
                        "forced one space straight backwards to an unoccupied space at any level.","Bull-headed Monster",JOptionPane.PLAIN_MESSAGE);
            }
        });
        panButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Win Condition: You also win if your Worker moves down two or more levels.","God of the Wild",JOptionPane.PLAIN_MESSAGE);
            }
        });
        prometheusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Your Turn: If your Worker does not move up, it may build both before and after moving.","Titan Benefactor of Mankind",JOptionPane.PLAIN_MESSAGE);
            }
        });
        humanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"No power","Mortal",JOptionPane.PLAIN_MESSAGE);
            }
        });

    }


    private static String getSelectedDivinity(JCheckBox[] boxArray) {
        String selecdiv = "";
        for (JCheckBox box : boxArray) {
            if (box.isSelected() == true) { 
                if (selecdiv.length() > 0) {
                    selecdiv = selecdiv + ",";
                }
                selecdiv = selecdiv + box.getText(); 
            }
        }
        return selecdiv;
    }
}
