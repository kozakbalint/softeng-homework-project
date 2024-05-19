package foxgame.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the Fox Game application.
 */
public class FoxGameApplication extends Application {

    /**
     * Start the JavaFX application.
     *
     * @param stage the primary stage for this application
     * @throws IOException if am error occurs during the loading of the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        Image icon = new Image(getClass().getResource("/icon.png").toString());
        stage.getIcons().add(icon);
        stage.setTitle("Fox Game");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
