package it.polimi.ingsw.client.view.GUI;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class LoginController extends GUIController {


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
                    switchScene(root, "/scenes/ChoseGod.fxml");
                }
            } catch (NumberFormatException e) {
                messageText.setText("Invalid port");
            }
        }
    }


}
