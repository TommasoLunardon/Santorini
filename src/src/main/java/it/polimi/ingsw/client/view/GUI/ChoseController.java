package it.polimi.ingsw.client.view.GUI;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class ChoseController implements Initializable {

    @FXML
    private Button selectButton;

    private static final Logger logger = Logger.getLogger(ChoseController.class.getName());
    private AlterView viewAlter;

    public void setApp(AlterView viewAlter) {
        this.viewAlter = viewAlter;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}


