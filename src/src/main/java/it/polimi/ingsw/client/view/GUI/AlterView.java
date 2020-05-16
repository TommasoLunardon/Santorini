
package it.polimi.ingsw.client.view.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The clientGui class launch the gui.
 */
public class AlterView extends Application {

    private static final Logger logger = Logger.getLogger(AlterView.class.getName());
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Santorini");
        gotoLogin();
        stage.show();

    }
    public void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent(StaticResourcesConfig.LOGIN_VIEW_PATH);
            login.setApp(this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void gotoChose() {
        try {
            ChoseController chose = (ChoseController) replaceSceneContent(StaticResourcesConfig.CHOSE_VIEW_PATH);
            chose.setApp(this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }


    /**
     * The main method
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * change View
     */
    private Initializable replaceSceneContent(String fxml) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        InputStream in = AlterView.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(AlterView.class.getResource(fxml));
        try {
            AnchorPane page = (AnchorPane) loader.load(in);
            Scene scene = new Scene(page, StaticResourcesConfig.STAGE_WIDTH, StaticResourcesConfig.STAGE_HEIGHT);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error！");
        } finally {
            in.close();
        }
        return (Initializable) loader.getController();
    }

}
