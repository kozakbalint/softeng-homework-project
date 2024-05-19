package foxgame.util;

import java.time.ZonedDateTime;

/**
 * Represents the result of a game.
 *
 * @param playerOneName the name of player one
 * @param playerTwoName the name of player two
 * @param winner the name of the winner
 * @param timestamp the time the game ended
 */
public record GameResult(String playerOneName, String playerTwoName, String winner, ZonedDateTime timestamp) {
}
