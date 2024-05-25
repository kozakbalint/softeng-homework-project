package foxgame.util;

import com.google.gson.Gson;
import org.tinylog.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class that manages the loading and saving of game states in JSON format.
 */
public class JsonGameStateManager implements GameStateManager{
    private Gson gson = new Gson();
    @Override
    public GameState loadState(String path) throws IOException {
        try (var reader = new FileReader(path)) {
            return gson.fromJson(reader, GameState.class);
        } catch (IOException e) {
            Logger.error(e, "Error loading game state");
            throw new IOException("Error loading game state", e);
        }

    }

    @Override
    public void saveState(GameState gameState, String savePath) throws IOException {
        try (var writer = new FileWriter(savePath)) {
            gson.toJson(gameState, writer);
        } catch (IOException e) {
            Logger.error(e, "Error saving game state");
            throw new IOException("Error saving game state", e);
        }
    }
}
