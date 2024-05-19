package foxgame.util;

import java.time.ZonedDateTime;

public record GameResult(String playerOneName, String playerTwoName, String winner, ZonedDateTime timestamp) {
}
