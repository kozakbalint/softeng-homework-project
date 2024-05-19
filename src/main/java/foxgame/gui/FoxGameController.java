package foxgame.gui;

import foxgame.model.FoxGameState;
import foxgame.model.Piece;
import foxgame.model.Position;
import foxgame.util.GameState;
import foxgame.util.GameStateManager;
import game.State;
import game.util.TwoPhaseMoveSelector;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.tinylog.Logger;
import puzzle.TwoPhaseMoveState;
import util.javafx.EnumImageStorage;
import util.javafx.ImageStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


public class FoxGameController {
    ObservableList<ListViewItem> items;
    @FXML
    private GridPane board;
    @FXML
    private ListView<ListViewItem> moveHistory;
    private ArrayList<ListViewItem> moves;
    private FoxGameState gameState;

    private TwoPhaseMoveSelector<Position> moveSelector;

    private ImageStorage<Piece> imageStorage = new EnumImageStorage<>(Piece.class);

    private GameStateManager gameStateManager;

    private File savePath;

    @FXML
    private void initialize() {
        board.getChildren().clear();
        moves = new ArrayList<>();
        items = FXCollections.observableArrayList(moves);
        gameState = new FoxGameState();
        moveSelector = new TwoPhaseMoveSelector<>(gameState);
        gameStateManager = new GameStateManager();
        savePath = new File("");

        moveHistory.setItems(items);
        moveHistory.getStyleClass().add("list-view");
        moveHistory.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ListViewItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    return;
                }
                if (item.move() != null) {
                    setText(String.format("from: %s\tto: %s", item.move().from(), item.move().to()));
                } else {
                    setText(item.message());
                }
            }
        });

        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
    }

    @FXML
    private void onNew() {
        Logger.debug("New Game started.");
        initialize();
    }

    @FXML
    private void onSave() {
        if (!savePath.getPath().isEmpty()) {
            try {
                gameStateManager.saveState(new GameState(savePath.getName(), items.stream().map(ListViewItem::move).filter(Objects::nonNull).toList()), savePath.getPath());
                return;
            } catch (Exception e) {
                Logger.error("Failed to save game state to file: {}", savePath);
            }
        }
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        var file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Logger.debug("Saving file: {}", file);
                gameStateManager.saveState(new GameState(file.getName(), items.stream().map(ListViewItem::move).filter(Objects::nonNull).toList()), file.getPath());
                savePath = new File(file.getAbsolutePath());
            } catch (Exception e) {
                Logger.error("Failed to save game state to file: {}", file);
            }
        }
    }

    @FXML
    private void onLoad() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        var file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Logger.debug("Opening file: {}", file);
                GameState loadState = gameStateManager.loadState(file.getPath());
                initialize();
                gameStateManager.applyState(gameState, loadState);
                savePath = new File(file.getAbsolutePath());
                for (int i = 0; i < loadState.moves().size(); i++) {
                    items.add(new ListViewItem(null, loadState.moves().get(i)));
                }
                if (gameState.isGameOver()) gameOver();
            } catch (Exception e) {
                Logger.error("Failed to load game state from file: {}", file);
            }
        }
    }

    @FXML
    private void onQuit() {
        Logger.debug("Terminating");
        Platform.exit();
    }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        if ((i + j) % 2 == 0)
            square.getStyleClass().add("square-white");
        else
            square.getStyleClass().add("square-black");
        var imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.imageProperty().bind(
                new ObjectBinding<>() {
                    {
                        super.bind(gameState.pieceProperty(i, j));
                    }

                    @Override
                    protected Image computeValue() {
                        return imageStorage.get(gameState.pieceProperty(i, j).get()).orElse(null);
                    }
                }
        );
        square.getChildren().add(imageView);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var pos = new Position(row, col);

        if (!gameState.isGameOver())
            select(pos);

        if (moveSelector.isReadyToMove()) {
            Logger.debug("Move from: {}, to: {}", moveSelector.getFrom(), moveSelector.getTo());
            gameState.makeMove(moveSelector.getFrom(), moveSelector.getTo());
            var move = new TwoPhaseMoveState.TwoPhaseMove<>(moveSelector.getFrom(), moveSelector.getTo());
            items.add(new ListViewItem(null, move));
            reset();
        }

        if (gameState.isGameOver()) {
            gameOver();
        }
    }

    private void gameOver() {
        Logger.debug("Game Over");
        removeMouseEventHandler();
        moveHistory.getItems().add(new ListViewItem(gameState.getStatus().toString(), null));
        openModal();
    }

    private void removeMouseEventHandler() {
        for (int i = 0; i < FoxGameState.BOARD_SIZE; i++) {
            for (int j = 0; j < FoxGameState.BOARD_SIZE; j++) {
                var square = getSquare(new Position(i, j));
                square.setOnMouseClicked(null);
            }
        }
    }

    private void select(Position position) {
        moveSelector.select(position);
        if (moveSelector.isInvalidSelection()) {
            Logger.debug("Invalid selection: {}", position);
            reset();
            return;
        }
        Logger.debug("Selected: {}", position);
        if (moveSelector.getPhase() == TwoPhaseMoveSelector.Phase.SELECT_TO && !gameState.isGameOver()) {
            var legalMoves = gameState.getLegalMoves(position, gameState.getNextPlayer());
            var square = getSquare(position);
            square.getStyleClass().add("square-selected");
            for (var move : legalMoves) {
                var nextSquare = getSquare(move);
                nextSquare.getChildren().add(makeCircle(nextSquare.getWidth(), nextSquare.getHeight()));
            }
        }
    }

    private void reset() {
        for (int i = 0; i < FoxGameState.BOARD_SIZE; i++) {
            for (int j = 0; j < FoxGameState.BOARD_SIZE; j++) {
                var square = getSquare(new Position(i, j));
                square.getChildren().removeIf(node -> node.getStyleClass().contains("square-next"));
                square.getStyleClass().remove("square-selected");
            }
        }

        moveSelector.reset();
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private Circle makeCircle(double w, double h) {
        double maxRadius = Math.min(w, h) / 2.0;
        var circle = new Circle(maxRadius * 0.4);
        circle.getStyleClass().add("square-next");
        return circle;
    }

    private void openModal() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        if (gameState.getStatus() == State.Status.PLAYER_1_WINS) {
            alert.setContentText("Player one won.");
        } else {
            alert.setContentText("Player two won.");
        }

        alert.showAndWait();
    }
}
