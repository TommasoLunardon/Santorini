
package it.polimi.ingsw.client.view.GUI;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The initialView class launch the gui.
 */

public class initialView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/initialView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Santorini");
        Scene initialScene = new Scene(root);
        primaryStage.setScene(initialScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    /**
     * The main method
     */
    public static void main(String[] args) {
        launch(args);
    }


}
