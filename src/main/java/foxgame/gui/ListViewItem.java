package foxgame.gui;

import foxgame.model.Position;
import puzzle.TwoPhaseMoveState;

/**
 * Represents an item in a list view.
 *
 * @param message the message of the item
 * @param move    the move of the item that is a move in the game
 */
public record ListViewItem(String message, TwoPhaseMoveState.TwoPhaseMove<Position> move) {
}
