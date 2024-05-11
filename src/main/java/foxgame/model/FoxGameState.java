package foxgame.model;

import game.TwoPhaseMoveState;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.ArrayList;

public class FoxGameState implements TwoPhaseMoveState<Position> {
    public static final int BOARD_SIZE = 8;
    private ReadOnlyObjectWrapper<Piece>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private Player player;

    public FoxGameState() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(Piece.EMPTY);
            }
        }
        board[0][2] = new ReadOnlyObjectWrapper<>(Piece.FOX);
        for (int i = 1; i < 8; i+=2) {
            board[7][i] = new ReadOnlyObjectWrapper<>(Piece.DOG);
        }
        player = Player.PLAYER_1;
    }

    @Override
    public boolean isLegalToMoveFrom(Position from) {
        if (isPlayerOne())
            return isOnBoard(from) && isPiece(from, Piece.FOX) && hasLegalMove(from, player);
        else
            return isOnBoard(from) && isPiece(from, Piece.DOG) && hasLegalMove(from, player);
    }

    @Override
    public boolean isLegalMove(Position from, Position to) {
        if (isPlayerOne())
            return isOnBoard(from) && isPiece(from, Piece.FOX) && isOnBoard(to) && isEmpty(to) && isDiagonalMove(from, to);
        else
            return isOnBoard(from) && isPiece(from, Piece.DOG) && isOnBoard(to) && isEmpty(to) && isForwardDiagonalMove(from, to);
    }

    @Override
    public void makeMove(Position from, Position to) {
        Piece fromPiece = board[from.row()][from.col()].get();
        board[from.row()][from.col()].set(Piece.EMPTY);
        board[to.row()][to.col()].set(fromPiece);
        player = player.opponent();
    }

    @Override
    public Player getNextPlayer() {
        return player;
    }

    @Override
    public boolean isGameOver() {
        var foxPosition = getFoxPosition();
        if (!hasLegalMove(foxPosition, Player.PLAYER_1)) return true;
        var dogPositions = getDogsPosition();
        var lastDogRow = dogPositions.stream().map(Position::row).max(Integer::compareTo).orElseThrow();
        return foxPosition.row() >= lastDogRow;
    }

    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            return Status.IN_PROGRESS;
        }
        return isGameOver() && !hasLegalMove(getFoxPosition(), Player.PLAYER_1) ? Status.PLAYER_2_WINS : Status.PLAYER_1_WINS;
    }

    public ReadOnlyObjectProperty<Piece> pieceProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    private Position getFoxPosition() {
        var position = new Position(-1, -1);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].get() == Piece.FOX) position = new Position(i, j);
            }
        }
        return position;
    }

    private ArrayList<Position> getDogsPosition() {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].get() == Piece.DOG) positions.add(new Position(i, j));
            }
        }
        return positions;
    }

    public ArrayList<Position> getLegalMoves(Position from, Player player) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = from.row() - 1; i <= from.row() + 1; i++) {
            for (int j = from.col() - 1; j <= from.col() + 1; j++) {
                if (i == from.row() && j == from.col()) {
                    continue;
                }
                if (isPlayerOne(player) && isOnBoard(i, j) && isDiagonalMove(from, new Position(i, j)) && isEmpty(i, j)) {
                    positions.add(new Position(i,j));
                }

                if (!isPlayerOne(player) && isOnBoard(i, j) && isForwardDiagonalMove(from, new Position(i, j)) && isEmpty(i, j)) {
                    positions.add(new Position(i,j));
                }
            }
        }
        return positions;
    }

    private boolean hasLegalMove(Position from, Player player) {
        for (int i = from.row() - 1; i <= from.row() + 1; i++) {
            for (int j = from.col() - 1; j <= from.col() + 1; j++) {
                if (i == from.row() && j == from.col()) {
                    continue;
                }
                if (isPlayerOne(player) && isOnBoard(i, j) && isDiagonalMove(from, new Position(i, j)) && isEmpty(i, j)) {
                    return true;
                }

                if (!isPlayerOne(player) && isOnBoard(i, j) && isForwardDiagonalMove(from, new Position(i, j)) && isEmpty(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPlayerOne() {
        return player == Player.PLAYER_1;
    }

    private boolean isPlayerOne(Player player) {
        return player == Player.PLAYER_1;
    }

    private boolean isOnBoard(Position p) {
        return isOnBoard(p.row(), p.col());
    }

    private boolean isOnBoard(int row, int col) {
        return 0 <= row && row < BOARD_SIZE && 0 <= col && col < BOARD_SIZE;
    }

    private boolean isEmpty(Position p) {
        return isEmpty(p.row(), p.col());
    }

    private boolean isEmpty(int row, int col) {
        return board[row][col].get() == Piece.EMPTY;
    }

    private boolean isPiece(Position pos, Piece piece) {
        return board[pos.row()][pos.col()].get() == piece;
    }

    private boolean isDiagonalMove(Position from, Position to) {
        int rowDiff = Math.abs(from.row() - to.row());
        int colDiff = Math.abs(from.col() - to.col());
        return rowDiff == 1 && colDiff == 1;
    }

    private boolean isForwardDiagonalMove(Position from, Position to) {
        int rowDiff = from.row() - to.row();
        int colDiff = Math.abs(from.col() - to.col());
        return rowDiff == 1 && colDiff == 1;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                switch (board[i][j].get()) {
                    case EMPTY -> sb.append("_ ");
                    case DOG -> sb.append("D ");
                    case FOX -> sb.append("F ");
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
