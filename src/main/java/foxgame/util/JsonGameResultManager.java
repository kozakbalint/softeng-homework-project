package foxgame.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link GameResultManager} that stores game results in a JSON file.
 */
public class JsonGameResultManager implements GameResultManager {
    private final String path;
    Type gameResultsType = new TypeToken<List<GameResult>>() {
    }.getType();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * Constructs a new {@code JsonGameResultManager}.
     * @param path the path to the JSON file
     */
    public JsonGameResultManager(String path) {
        this.path = path;
    }

    /**
     * Adds a game result to the manager.
     *
     * @param gameResult the {@link GameResult} to add
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void add(GameResult gameResult) throws IOException {
        var results = getGameResults();
        results.add(gameResult);
        try (var writer = new FileWriter(path)) {
            gson.toJson(results, writer);
        }
    }

    /**
     * {@return the list of all game results}
     * @throws IOException if an I/O error occurs
     */
    @Override
    public List<GameResult> getGameResults() throws IOException {
        if (!Files.exists(Path.of(path))) {
            return new ArrayList<>();
        }
        try (var reader = new FileReader(path)) {
            return gson.fromJson(reader, gameResultsType);
        }
    }
}
