package foxgame.util;

import com.google.gson.Gson;
import foxgame.model.FoxGameState;
import org.tinylog.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages the state of the game.
 */
public class GameStateManager {
    Gson gson;

    /**
     * Constructs a new {@code GameStateManager}.
     */
    public GameStateManager() {
        gson = new Gson();
    }

    /**
     * Loads the state of the game from the given path.
     *
     * @param path the path to load the state from
     * @return the loaded state
     * @throws IOException if an I/O error occurs
     */
    public GameState loadState(String path) throws IOException {
        GameState gameState;
        try (FileReader reader = new FileReader(path)) {
            gameState = gson.fromJson(reader, GameState.class);
            return gameState;
        } catch (IOException e) {
            Logger.error(e);
            throw new IOException("Failed to load state");
        }
    }

    /**
     * Saves the state of the game to the given path.
     *
     * @param gameState the state to save
     * @param savePath the path to save the state to
     * @throws IOException if an I/O error occurs
     */
    public void saveState(GameState gameState, String savePath) throws IOException {
        try (FileWriter writer = new FileWriter(savePath)) {
            gson.toJson(gameState, writer);
        } catch (IOException e) {
            Logger.error(e);
            throw new IOException("Failed to save state");
        }
    }

    /**
     * Applies the given state to the game state.
     *
     * @param gameState the game state to apply the state to
     * @param state the state to apply
     */
    public void applyState(FoxGameState gameState, GameState state) {
        for (var move : state.moves()) {
            if (gameState.isLegalMove(move.from(), move.to())) {
                gameState.makeMove(move.from(), move.to());
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
