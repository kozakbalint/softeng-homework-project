package foxgame.util;

import foxgame.model.Position;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameStateManagerTest {
    GameStateManager gameStateManager;
    GameState gameState;

    @Test
    void loadState_success() throws IOException {
        gameStateManager = new GameStateManager();
        gameState = gameStateManager.loadState(getClass().getResource("/test.json").getPath());
        assertEquals("test", gameState.name());
        assertEquals(3, gameState.moves().size());
        assertEquals(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 2), new Position(1, 3)), gameState.moves().get(0));
        assertEquals(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(7, 1), new Position(6, 0)), gameState.moves().get(1));
        assertEquals(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 3), new Position(2, 2)), gameState.moves().get(2));
    }

    @Test
    void saveState_success() throws IOException {
        gameStateManager = new GameStateManager();
        gameState = new GameState("test", "one", "two", new ArrayList<>());
        gameState.moves().add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 2), new Position(1, 3)));
        gameState.moves().add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(7, 1), new Position(6, 0)));
        gameState.moves().add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 3), new Position(2, 2)));
        gameStateManager.saveState(gameState, "test.json");
        GameState loadedGameState = gameStateManager.loadState("test.json");
        assertEquals(gameState, loadedGameState);
    }

    @Test
    void loadState_failure() {
        gameStateManager = new GameStateManager();
        assertThrows(IOException.class, () -> gameStateManager.loadState("nonexistent.json"));
    }

    @Test
    void saveState_failure() {
        gameStateManager = new GameStateManager();
        GameState gameState = new GameState("test.json", "one", "two", new ArrayList<>());
        assertThrows(IOException.class, () -> gameStateManager.saveState(gameState, "nonexistent/test.json"));
        assertThrows(IOException.class, () -> gameStateManager.saveState(gameState, "/usr/bin/"));
    }
}