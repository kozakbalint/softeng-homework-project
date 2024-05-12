package foxgame.gui;

import foxgame.model.Position;
import puzzle.TwoPhaseMoveState;

public record ListViewItem(String message, TwoPhaseMoveState.TwoPhaseMove<Position> move) {
}
