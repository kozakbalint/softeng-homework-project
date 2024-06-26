package foxgame.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * Controller for the main menu view.
 */
public class MenuController {

    @FXML
    private void onNewGame(ActionEvent actionEvent) {
        Logger.debug("Starting new game");
        try {
            loadStage("/newgame.fxml", actionEvent, null);
        } catch (IOException e) {
            Logger.error(e, "Failed to start new game");
        }
    }

    @FXML
    private void onLoadGame(ActionEvent actionEvent) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fox Game Save Files", "*.fox"));
        var file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Logger.debug("Opening file: {}", file);
            try {
                GameController controller = new GameController();
                controller.setSaveFile(file);
                loadStage("/game.fxml", actionEvent, controller);
            } catch (IOException e) {
                Logger.error(e, "Failed to load game");
            }
        }
    }

    @FXML
    private void onQuit(ActionEvent actionEvent) {
        Logger.debug("Terminating");
        Platform.exit();
    }

    private void loadStage(String fxmlPath, ActionEvent event, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        if (controller != null)
            fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
