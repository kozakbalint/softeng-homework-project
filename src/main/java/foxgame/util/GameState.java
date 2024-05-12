package foxgame.util;

import foxgame.model.Position;
import puzzle.TwoPhaseMoveState;

import java.util.List;

public record GameState(String name, List<TwoPhaseMoveState.TwoPhaseMove<Position>> moves) {
    @Override
    public String toString() {
        return String.format("name:%s, moves:%s", name, moves);
    }
}
