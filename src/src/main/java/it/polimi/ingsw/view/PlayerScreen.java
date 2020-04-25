package it.polimi.ingsw.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerScreen {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Santorini, the ultra magic special game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JButton cli = new JButton("select CLI");
        //JButton gui = new JButton("select GUI");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(cli, Component.CENTER_ALIGNMENT);
        cli.setActionCommand("cli");
        cli.addActionListener(new TypeListener());

        /*panel.add(gui, Component.CENTER_ALIGNMENT);
        gui.setActionCommand("gui");
        cli.addActionListener(new TypeListener());*/

        frame.add(panel);
        frame.setVisible(true);
    }
}

/**
 * @deprecated whe we defined gui possibility choice, have to implement an if
 */
class TypeListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("cli")){
            new PlayerInterfaceCLI();/*principal action to start a program whit CLI*/
        }
    }
}
