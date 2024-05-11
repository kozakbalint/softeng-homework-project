package foxgame.gui;

import com.sun.javafx.collections.ObservableListWrapper;
import foxgame.model.FoxGameState;
import foxgame.model.Piece;
import foxgame.model.Position;
import game.State;
import game.util.TwoPhaseMoveSelector;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;
import util.javafx.EnumImageStorage;
import util.javafx.ImageStorage;

import java.io.IOException;
import java.util.ArrayList;


public class FoxGameController {
    @FXML
    private GridPane board;

    @FXML
    private ListView<String> moveHistory;

    private int moveNum;

    private ArrayList<String> itemList = new ArrayList<>();

    ObservableListWrapper<String> items = new ObservableListWrapper<>(itemList);

    private FoxGameState gameState = new FoxGameState();

    private TwoPhaseMoveSelector<Position> moveSelector = new TwoPhaseMoveSelector<>(gameState);

    private ImageStorage<Piece> imageStorage = new EnumImageStorage<>(Piece.class);

    @FXML
    private void initialize() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i ,j);
                board.add(square, j, i);
            }
        }

        moveNum = 1;
        moveHistory.setItems(items);
        moveHistory.getStyleClass().add("list-view");
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
                new ObjectBinding<Image>() {
                    {
                        super.bind(gameState.pieceProperty(i,j));
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
        Logger.info("Clicked on " + pos);

        if (!gameState.isGameOver())
            select(pos);

        if (moveSelector.isInvalidSelection()) {
            reset();
            return;
        }
        if (moveSelector.isReadyToMove()) {
            gameState.makeMove(moveSelector.getFrom(), moveSelector.getTo());
            items.add(String.format("%d. %s -> %s", moveNum, moveSelector.getFrom(), moveSelector.getTo()));
            moveNum += 1;
            reset();
        }

        if (gameState.isGameOver()) {
            openModal();
        }
    }

    private void select(Position position) {
        moveSelector.select(position);
        if (moveSelector.getPhase() == TwoPhaseMoveSelector.Phase.SELECT_TO && !gameState.isGameOver()) {
            var legalMoves = gameState.getLegalMoves(position,gameState.getNextPlayer());
            var square = getSquare(position);
            square.getStyleClass().add("square-selected");
            for (var move: legalMoves) {
                var nextSquare = getSquare(move);
                nextSquare.getChildren().add(makeCircle(nextSquare.getWidth(), nextSquare.getHeight()));
            }
        }
    }

    private void reset(){
        for (var child : board.getChildren()) {
            var nextMove = (StackPane) child;
            nextMove.getChildren().removeIf(node -> node.getStyleClass().contains("square-next"));
            child.getStyleClass().remove("square-selected");
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
        double maxRadius = Math.min(w,h) / 2.0;
        var circle = new Circle(maxRadius * 0.4);
        circle.getStyleClass().add("square-next");
        return circle;
    }

    private void openModal()  {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        if (gameState.getStatus() == State.Status.PLAYER_1_WINS) {
            alert.setContentText("Player One won.");
        } else {
            alert.setContentText("Player two won.");
        }

        alert.showAndWait();
    }
}