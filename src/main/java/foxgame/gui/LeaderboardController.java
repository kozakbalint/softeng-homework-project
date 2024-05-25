package foxgame.gui;

import foxgame.util.JsonGameResultManager;
import foxgame.util.PlayerStats;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * Controller for the leaderboard view.
 */
public class LeaderboardController {
    @FXML
    private TableView<PlayerStats> tableView;
    @FXML
    private TableColumn<PlayerStats, String> tableNameCol;
    @FXML
    private TableColumn<PlayerStats, Long> tableWinsCol;

    @FXML
    private void initialize() {
        Logger.debug("Initializing leaderboard");
        tableNameCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().name()));
        tableWinsCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().wins()));
        ObservableList<PlayerStats> results = FXCollections.observableArrayList();
        try {
            JsonGameResultManager gameResultManager = new JsonGameResultManager("results.json");
            results.addAll(gameResultManager.getBestPlayers(10));
            tableView.setItems(results);
        } catch (IOException e) {
            Logger.error("Failed to load leaderboard: {}", e.getMessage());
        }
    }
    @FXML
    private void onNewGame(ActionEvent event) {
        Logger.debug("Starting new game");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/newgame.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.error(e, "Failed to start new game");
        }
    }

    @FXML
    private void onQuit(ActionEvent event) {
        Logger.debug("Terminating");
        Platform.exit();
    }
}
