package foxgame.util;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages the results of games.
 */
public interface GameResultManager {

    /**
     * Adds a game result to the manager.
     *
     * @param gameResult the {@link GameResult} to add
     * @throws IOException if an I/O error occurs
     */
    void add(GameResult gameResult) throws IOException;

    /**
     * {@return the list of all game results}
     * @throws IOException if an I/O error occurs
     */
    List<GameResult> getGameResults() throws IOException;

    /**
     * {@return the list of the best players, sorted by the number of wins in descending order, limited to {@code limit} players}
     * @param limit the number of players to return
     * @throws IOException if an I/O error occurs
     */
    default List<PlayerStats> getBestPlayers(int limit) throws IOException {
        Map<String, Long> winCounts = getGameResults().stream()
                .collect(Collectors.groupingBy(GameResult::winner, Collectors.counting()));
        return winCounts.entrySet().stream()
                .map(entry -> new PlayerStats(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(PlayerStats::wins).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
