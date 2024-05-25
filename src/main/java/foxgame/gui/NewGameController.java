package foxgame.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * Controller for the new game view.
 */
public class NewGameController {
    @FXML
    private Button newgameButton;

    @FXML
    private TextField playerOneNameField;

    @FXML
    private TextField playerTwoNameField;

    @FXML
    private void initialize() {
        newgameButton.setDisable(true);
        playerTwoNameField.setDisable(true);
        
        playerOneNameField.textProperty().addListener((observable, oldValue, newValue) -> playerTwoNameField.setDisable(newValue.isEmpty()));
        playerTwoNameField.textProperty().addListener((observable, oldValue, newValue) -> newgameButton.setDisable(newValue.isEmpty()));
    }

    @FXML
    private void onNewGame(ActionEvent event) throws IOException {
        Logger.debug("Starting new game with players {} and {}", playerOneNameField.getText(), playerTwoNameField.getText());
        GameController controller = new GameController();
        controller.setPlayerNames(playerOneNameField.getText(), playerTwoNameField.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/game.fxml"));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
