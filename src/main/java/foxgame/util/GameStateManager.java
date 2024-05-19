package foxgame.util;

import com.google.gson.Gson;
import foxgame.model.FoxGameState;
import org.tinylog.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameStateManager {
    Gson gson;

    public GameStateManager() {
        gson = new Gson();
    }


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

    public void saveState(GameState gameState, String savePath) throws IOException {
        try (FileWriter writer = new FileWriter(savePath)) {
            gson.toJson(gameState, writer);
        } catch (IOException e) {
            Logger.error(e);
            throw new IOException("Failed to save state");
        }
    }

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
