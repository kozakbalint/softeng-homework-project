package foxgame.util;

import com.google.gson.Gson;
import foxgame.model.FoxGameState;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StateManager {
    Gson gson;

    public StateManager() {
        gson = new Gson();
    }

    public GameState loadState(String path) {
        GameState gameState = new GameState(null, null);
        try (FileReader reader = new FileReader(path)) {
            gameState = gson.fromJson(reader, GameState.class);
        } catch (IOException e) {
            Logger.error(e);
        }

        return gameState;
    }

    public void saveState(GameState gameState, String savePath) {
        try (FileWriter writer = new FileWriter(savePath)) {
            gson.toJson(gameState, writer);
        } catch (IOException e) {
            Logger.error(e);
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
