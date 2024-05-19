package foxgame.util;

import foxgame.model.Position;
import puzzle.TwoPhaseMoveState;

import java.util.List;

public record GameState(String name, String playerOneName, String playerTwoName,
                        List<TwoPhaseMoveState.TwoPhaseMove<Position>> moves) {
    @Override
    public String toString() {
        return String.format("name:%s, playerOne:%s, playerTwo:%s, moves:%s", name, playerOneName, playerTwoName, moves);
    }
}
