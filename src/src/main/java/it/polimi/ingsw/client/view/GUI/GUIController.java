package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class GUIController {
    private double initialX = 0;
    private double initialY = 0;

    /**
     * The addDraggableNode method is used to permit
     * a node to be draggable.
     */

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
    /**
     * The switchScene method is used to switch the scene
     */

    protected void switchScene(AnchorPane root, String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent secondView = loader.load();
            Scene newScene = new Scene(secondView);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setWidth(1024);
            stage.setHeight(624);
            stage.centerOnScreen();
            stage.setScene(newScene);
        } catch (IOException e1) {
            Logger.getGlobal().warning(e1.getCause().toString());
        }
    }
}
