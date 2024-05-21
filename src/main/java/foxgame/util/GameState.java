package foxgame.util;

import foxgame.model.Position;
import puzzle.TwoPhaseMoveState;

import java.util.List;

/**
 * Represents the state of a game.
 *
 * @param name          the name of the game
 * @param playerOneName the name of player one
 * @param playerTwoName the name of player two
 * @param moves         the moves made in the game
 */
public record GameState(String name, String playerOneName, String playerTwoName,
                        List<TwoPhaseMoveState.TwoPhaseMove<Position>> moves) {
    @Override
    public String toString() {
        return String.format("name:%s, playerOne:%s, playerTwo:%s, moves:%s", name, playerOneName, playerTwoName, moves);
    }
}
