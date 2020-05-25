package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.util.ArrayList;


//ATTENZIONE questa è una classe di supporto al test facendo le veci di PlayerInterface
//la cosa importante è cambiare l'inestazione della classe come sotto incollare il costruttore in quello di playerInterface
public class CustomFrame extends JFrame implements Runnable{
    private MapGUI centralMap;
    private LeftMenu leftMenu;
    private JSplitPane fundamentalContainer;

@Override
    public void run() {

        ArrayList<Integer> coordinates;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);

        centralMap = new MapGUI();

        leftMenu = new LeftMenu(centralMap);


        fundamentalContainer = new JSplitPane();

        fundamentalContainer.setLeftComponent(leftMenu);
        fundamentalContainer.setRightComponent(centralMap);

        centralMap.getBox(0,0).setLevel(4);
        //coordinates=centralMap.selectBox();


//        System.out.println(coordinates.get(0)+coordinates.get(1));

        pack();
        add(fundamentalContainer);
    }

    public CustomFrame(String name, int width, int height) {
        super(name);
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("santorini_risorse-grafiche-2/Sprite/Background #19028.png"));
        setIconImage(icon.getImage());
        run();
    }

}
