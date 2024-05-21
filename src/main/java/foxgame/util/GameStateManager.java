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
public interface GameStateManager {

    /**
     * Loads the state of the game from the given path.
     *
     * @param path the path to load the state from
     * @return the loaded state
     * @throws IOException if an I/O error occurs
     */
    GameState loadState(String path) throws IOException;

    /**
     * Saves the state of the game to the given path.
     *
     * @param gameState the state to save
     * @param savePath  the path to save the state to
     * @throws IOException if an I/O error occurs
     */
    void saveState(GameState gameState, String savePath) throws IOException;

    /**
     * Applies the given state to the game state.
     *
     * @param gameState the game state to apply the state to
     * @param state     the state to apply
     */
    default void applyState(FoxGameState gameState, GameState state) {
        for (var move : state.moves()) {
            if (gameState.isLegalMove(move.from(), move.to())) {
                gameState.makeMove(move.from(), move.to());
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
