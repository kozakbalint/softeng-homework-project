package foxgame.util;

import foxgame.model.FoxGameState;
import foxgame.model.Position;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonGameStateManagerTest {
    GameStateManager gameStateManager;
    GameState gameState;

    @Test
    void loadState_success() throws IOException {
        gameStateManager = new JsonGameStateManager();
        gameState = gameStateManager.loadState(getClass().getResource("/test.json").getPath());
        assertEquals("test", gameState.name());
        assertEquals(3, gameState.moves().size());
        assertEquals(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 2), new Position(1, 3)), gameState.moves().get(0));
        assertEquals(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(7, 1), new Position(6, 0)), gameState.moves().get(1));
        assertEquals(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 3), new Position(2, 2)), gameState.moves().get(2));
    }

    @Test
    void saveState_success() throws IOException {
        gameStateManager = new JsonGameStateManager();
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
        gameStateManager = new JsonGameStateManager();
        assertThrows(IOException.class, () -> gameStateManager.loadState("nonexistent.json"));
    }

    @Test
    void saveState_failure() {
        gameStateManager = new JsonGameStateManager();
        GameState gameState = new GameState("test.json", "one", "two", new ArrayList<>());
        assertThrows(IOException.class, () -> gameStateManager.saveState(gameState, "nonexistent/test.json"));
        assertThrows(IOException.class, () -> gameStateManager.saveState(gameState, "/usr/bin/"));
    }

    @Test
    void applyState_success() throws IOException {
        gameStateManager = new JsonGameStateManager();
        FoxGameState gameState = new FoxGameState();
        FoxGameState gameState2 = new FoxGameState();
        GameState loadedGameState = gameStateManager.loadState(getClass().getResource("/test.json").getPath());
        gameStateManager.applyState(gameState, loadedGameState);
        gameState2.makeMove(loadedGameState.moves().get(0).from(), loadedGameState.moves().get(0).to());
        gameState2.makeMove(loadedGameState.moves().get(1).from(), loadedGameState.moves().get(1).to());
        gameState2.makeMove(loadedGameState.moves().get(2).from(), loadedGameState.moves().get(2).to());

        assertEquals(gameState, gameState2);
    }

    @Test
    void applyState_failure() throws IOException {
        gameStateManager = new JsonGameStateManager();
        FoxGameState gameState = new FoxGameState();
        GameState loadedGameState = gameStateManager.loadState(getClass().getResource("/test_bad.json").getPath());
        assertThrows(IllegalStateException.class, () -> gameStateManager.applyState(gameState, loadedGameState));
    }
}