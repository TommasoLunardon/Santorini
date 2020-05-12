package it.polimi.ingsw.client.view;

import javax.swing.*;
import java.awt.*;

public class BoxGUI extends JButton  implements Box{
    private Image icon;
    public BoxGUI (){
        super();
        icon=new ImageIcon(getClass().getResource())
    }
}
