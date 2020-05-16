package it.polimi.ingsw.client.view.GUI;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Text messageText;
    @FXML
    private TextField nameText;
    @FXML
    private Button playButton;
    @FXML
    private TextField portText;

    private AlterView viewAlter;


    /**
     *
     * The initialize method is used to initialize the
     * initial view scene.
     *
     */

    public void initialize() {
        final String textEffect = "textEffect";
        final String playButtonEffect = "playButton";
        portText.getStyleClass().add(textEffect);
        nameText.getStyleClass().add(textEffect);
        playButton.getStyleClass().add(playButtonEffect);
        addDraggableNode(root);
    }

    /**
     *
     * The getName method is used to take the player's name
     *
     */

    @FXML
    public void getName() {
        if (!nameText.getText().equals("")) {
            nameText.getText();
        } else {
            messageText.setText("Please insert a name");
        }
    }

    /**
     *
     * The getConnection method is used to connect to the server.
     *
     */

    @FXML
    public void getConnection() {
        if (portText.getText().equals("")) {
            messageText.setText("Insert: " + (portText.getText().equals("") ? "Port" : "") );
        } else {
            try {
                messageText.setText("");
                Integer.parseInt(portText.getText());
                portText.getText();
                if("123456".equalsIgnoreCase(portText.getText())){
                    viewAlter.gotoChose();
                }
            } catch (NumberFormatException e) {
                messageText.setText("Invalid port");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setApp(AlterView viewAlter) {
        this.viewAlter = viewAlter;
    }

    /**
     * The addDraggableNode method is used to permit
     * a node to be draggable.
     *
     */
    private double initialX = 0;
    private double initialY = 0;

    public void addDraggableNode(final Node node) {

        node.setOnMousePressed(me -> {
            if (me.getButton() != MouseButton.MIDDLE) {
                initialX = me.getSceneX();
                initialY = me.getSceneY();
            }
        });

        node.setOnMouseDragged(me -> {
            if (me.getButton() != MouseButton.MIDDLE) {
                node.getScene().getWindow().setX(me.getScreenX() - initialX);
                node.getScene().getWindow().setY(me.getScreenY() - initialY);
            }
        });
    }
}
