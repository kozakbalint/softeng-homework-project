package foxgame.util;

/**
 * Represents the statistics of a player.
 *
 * @param name the name of the player
 * @param wins the number of wins the player has
 */
public record PlayerStats(String name, Long wins) {
}
