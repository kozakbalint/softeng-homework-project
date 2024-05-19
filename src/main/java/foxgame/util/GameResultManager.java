package foxgame.util;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface GameResultManager {
    void add(GameResult gameResult) throws IOException;

    List<GameResult> getGameResults() throws IOException;

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
