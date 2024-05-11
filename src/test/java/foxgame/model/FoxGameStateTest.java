package foxgame.model;

import game.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoxGameStateTest {
    private FoxGameState gameState;

    private void setupFoxLostState(FoxGameState gameState){
        gameState.makeMove(new Position(0,2), new Position(1,1));
        gameState.makeMove(new Position(7,1), new Position(6,0));
        gameState.makeMove(new Position(1,1), new Position(0,0));
        gameState.makeMove(new Position(6,0), new Position(5,1));
        gameState.makeMove(new Position(0,0), new Position(1,1));
        gameState.makeMove(new Position(5,1), new Position(4,0));
        gameState.makeMove(new Position(1,1), new Position(0,0));
        gameState.makeMove(new Position(4,0), new Position(3,1));
        gameState.makeMove(new Position(0,0), new Position(1,1));
        gameState.makeMove(new Position(3,1), new Position(2,0));
        gameState.makeMove(new Position(1,1), new Position(0,0));
        gameState.makeMove(new Position(2,0), new Position(1,1));
    }

    private void setupDogsLostState(FoxGameState gameState) {
        gameState.makeMove(new Position(0,2), new Position(1,1));
        gameState.makeMove(new Position(7,3), new Position(6,2));
        gameState.makeMove(new Position(1,1), new Position(2,2));
        gameState.makeMove(new Position(6,2), new Position(5,3));
        gameState.makeMove(new Position(2,2), new Position(3,3));
        gameState.makeMove(new Position(5,3), new Position(4,2));
        gameState.makeMove(new Position(3,3), new Position(4,4));
        gameState.makeMove(new Position(4,2), new Position(3,3));
        gameState.makeMove(new Position(4,4), new Position(5,3));
        gameState.makeMove(new Position(7,1), new Position(6,0));
        gameState.makeMove(new Position(5,3), new Position(6,2));
        gameState.makeMove(new Position(7,5), new Position(6,4));
        gameState.makeMove(new Position(6,2), new Position(7,3));
        gameState.makeMove(new Position(7,7), new Position(6,6));
    }

    @Test
    void isLegalToMoveFrom_PlayerOne_True() {
        gameState = new FoxGameState();
        assertTrue(gameState.isLegalToMoveFrom(new Position(0,2)));
    }

    @Test
    void isLegalToMoveFrom_PlayerOne_False() {
        gameState = new FoxGameState();
        assertFalse(gameState.isLegalToMoveFrom(new Position(0,0)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(7,7)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(-1,1)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(1,-1)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(-1,-1)));
    }

    @Test
    void isLegalToMoveFrom_PlayerTwo_True() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        assertTrue(gameState.isLegalToMoveFrom(new Position(7,1)));
        assertTrue(gameState.isLegalToMoveFrom(new Position(7,3)));
        assertTrue(gameState.isLegalToMoveFrom(new Position(7,5)));
        assertTrue(gameState.isLegalToMoveFrom(new Position(7,7)));
    }

    @Test
    void isLegalToMoveFrom_PlayerTwo_False() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        assertFalse(gameState.isLegalToMoveFrom(new Position(1,1)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(0,0)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(-1,1)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(1,-1)));
        assertFalse(gameState.isLegalToMoveFrom(new Position(-1,-1)));
    }

    @Test
    void isLegalMove_PlayerOne_True() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        gameState.makeMove(new Position(7,1), new Position(6,0));
        Position from = new Position(1,1);
        assertTrue(gameState.isLegalMove(from, new Position(0,0)));
        assertTrue(gameState.isLegalMove(from, new Position(0,2)));
        assertTrue(gameState.isLegalMove(from, new Position(2,2)));
        assertTrue(gameState.isLegalMove(from, new Position(2,0)));
    }

    @Test
    void isLegalMove_PlayerOne_False() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        gameState.makeMove(new Position(7,1), new Position(6,0));
        Position from1 = new Position(1,1);
        assertFalse(gameState.isLegalMove(from1, new Position(0,1)));
        assertFalse(gameState.isLegalMove(from1, new Position(1, 0)));
        assertFalse(gameState.isLegalMove(from1, new Position(7, 7)));
        assertFalse(gameState.isLegalMove(from1, new Position(-1,-1)));
    }

    @Test
    void isLegalMove_PlayerTwo_True() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        Position from = new Position(7,3);
        assertTrue(gameState.isLegalMove(from, new Position(6,2)));
        assertTrue(gameState.isLegalMove(from, new Position(6,4)));
    }

    @Test
    void isLegalMove_PlayerTwo_False() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        Position from = new Position(7,7);
        assertFalse(gameState.isLegalMove(from, new Position(6,7)));
        assertFalse(gameState.isLegalMove(from, new Position(7, 6)));
        assertFalse(gameState.isLegalMove(from, new Position(7, 8)));
        assertFalse(gameState.isLegalMove(from, new Position(8,8)));
    }

    @Test
    void makeMove() {
    }

    @Test
    void getNextPlayer_PlayerOne_True() {
        gameState = new FoxGameState();
        assertEquals(State.Player.PLAYER_1, gameState.getNextPlayer());
        gameState.makeMove(new Position(0,2), new Position(1,1));
        gameState.makeMove(new Position(7,7), new Position(6,6));
        assertEquals(State.Player.PLAYER_1, gameState.getNextPlayer());
    }

    @Test
    void getNextPlayer_PlayerTwo_True() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        assertEquals(State.Player.PLAYER_2, gameState.getNextPlayer());
        gameState.makeMove(new Position(7,7), new Position(6,6));
        gameState.makeMove(new Position(1,1), new Position(0,0));
        assertEquals(State.Player.PLAYER_2, gameState.getNextPlayer());
    }

    @Test
    void isGameOver_PlayerOne_Lost() {
        gameState = new FoxGameState();
        setupFoxLostState(gameState);
        assertTrue(gameState.isGameOver());
    }

    @Test
    void isGameOver_PlayerTwo_Lost() {
        gameState = new FoxGameState();
        setupDogsLostState(gameState);
        assertTrue(gameState.isGameOver());
    }

    @Test
    void getStatus_InProgress() {
        gameState = new FoxGameState();
        gameState.makeMove(new Position(0,2), new Position(1,1));
        assertEquals(State.Status.IN_PROGRESS, gameState.getStatus());
    }

    @Test
    void getStatus_PlayerOne_Lost() {
        gameState = new FoxGameState();
        setupFoxLostState(gameState);
        assertEquals(State.Status.PLAYER_2_WINS, gameState.getStatus());
    }

    @Test
    void getStatus_PlayerTwo_Lost() {
        gameState = new FoxGameState();
        setupDogsLostState(gameState);
        assertEquals(State.Status.PLAYER_1_WINS, gameState.getStatus());
    }

    @Test
    void testToString() {
        gameState = new FoxGameState();
        String baseState = """
                _ _ F _ _ _ _ _\s
                _ _ _ _ _ _ _ _\s
                _ _ _ _ _ _ _ _\s
                _ _ _ _ _ _ _ _\s
                _ _ _ _ _ _ _ _\s
                _ _ _ _ _ _ _ _\s
                _ _ _ _ _ _ _ _\s
                _ D _ D _ D _ D\s
                """;
        assertEquals(baseState, gameState.toString());
    }
}