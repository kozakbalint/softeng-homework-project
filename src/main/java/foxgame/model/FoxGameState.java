package foxgame.model;

import game.TwoPhaseMoveState;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the state of the game.
 */
public class FoxGameState implements TwoPhaseMoveState<Position> {

    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE = 8;
    private ReadOnlyObjectWrapper<Piece>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private Player player;

    /**
     * Constructs a new {@code FoxGameState} object that represents the initial state of the game.
     */
    public FoxGameState() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(Piece.EMPTY);
            }
        }
        board[0][2] = new ReadOnlyObjectWrapper<>(Piece.FOX);
        for (int i = 1; i < 8; i += 2) {
            board[7][i] = new ReadOnlyObjectWrapper<>(Piece.DOG);
        }
        player = Player.PLAYER_1;
    }

    /**
     * {@return whether it is legal to move from the given {@link Position}}
     *
     * @param from the {@link Position} to move from
     */
    @Override
    public boolean isLegalToMoveFrom(Position from) {
        if (isPlayerOne())
            return isOnBoard(from) && isPiece(from, Piece.FOX) && hasLegalMove(from, player);
        else
            return isOnBoard(from) && isPiece(from, Piece.DOG) && hasLegalMove(from, player);
    }

    /**
     * {@return whether it is legal to move from the given {@link Position} to another {@link Position}}
     *
     * @param from the {@link Position} to move from
     * @param to   the {@link Position} to move to
     */
    @Override
    public boolean isLegalMove(Position from, Position to) {
        if (isPlayerOne())
            return isOnBoard(from) && isPiece(from, Piece.FOX) && isOnBoard(to) && isEmpty(to) && isDiagonalMove(from, to);
        else
            return isOnBoard(from) && isPiece(from, Piece.DOG) && isOnBoard(to) && isEmpty(to) && isForwardDiagonalMove(from, to);
    }

    /**
     * Makes a move from the given {@link Position} to another {@link Position}.
     *
     * @param from the {@link Position} to move from
     * @param to   the {@link Position} to move to
     */
    @Override
    public void makeMove(Position from, Position to) {
        Piece fromPiece = board[from.row()][from.col()].get();
        board[from.row()][from.col()].set(Piece.EMPTY);
        board[to.row()][to.col()].set(fromPiece);
        player = player.opponent();
    }

    /**
     * {@return the {@link Player} whose turn currently is}
     */
    @Override
    public Player getNextPlayer() {
        return player;
    }

    /**
     * {@return whether the game is over or not}
     */
    @Override
    public boolean isGameOver() {
        var foxPosition = getPlayerPositions(Player.PLAYER_1).get(0);
        if (!hasLegalMove(foxPosition, Player.PLAYER_1)) return true;
        var dogPositions = getPlayerPositions(Player.PLAYER_2);
        var lastDogRow = dogPositions.stream().map(Position::row).max(Integer::compareTo).orElseThrow();
        return foxPosition.row() >= lastDogRow;
    }

    /**
     * {@return the {@link Status} of the game}
     */
    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            return Status.IN_PROGRESS;
        }
        return isGameOver() && !hasLegalMove(getPlayerPositions(Player.PLAYER_1).get(0), Player.PLAYER_1) ? Status.PLAYER_2_WINS : Status.PLAYER_1_WINS;
    }

    /**
     * {@return the {@link Piece} property at the given row and column index of the board}
     * @param i the row of the {@code board}
     * @param j the column of the {@code board}
     */
    public ReadOnlyObjectProperty<Piece> pieceProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * {@return the legal moves from the given {@link Position} for the given {@link Player}}
     * @param from   the {@link Position} to move from
     * @param player the {@link Player} to move
     */
    public ArrayList<Position> getLegalMoves(Position from, Player player) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = from.row() - 1; i <= from.row() + 1; i++) {
            for (int j = from.col() - 1; j <= from.col() + 1; j++) {
                if (i == from.row() && j == from.col()) {
                    continue;
                }
                if (isPlayerOne(player) && isOnBoard(i, j) && isDiagonalMove(from, new Position(i, j)) && isEmpty(i, j)) {
                    positions.add(new Position(i, j));
                }

                if (!isPlayerOne(player) && isOnBoard(i, j) && isForwardDiagonalMove(from, new Position(i, j)) && isEmpty(i, j)) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    private ArrayList<Position> getPlayerPositions(Player player) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isPlayerOne(player) && board[i][j].get() == Piece.FOX) positions.add(new Position(i, j));
                if (!isPlayerOne(player) && board[i][j].get() == Piece.DOG) positions.add(new Position(i, j));
            }
        }
        return positions;
    }

    private boolean hasLegalMove(Position from, Player player) {
        return !getLegalMoves(from, player).isEmpty();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoxGameState gameState = (FoxGameState) o;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].get() != gameState.board[i][j].get()) {
                    return false;
                }
            }
        }
        return player == gameState.player;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(player);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
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
