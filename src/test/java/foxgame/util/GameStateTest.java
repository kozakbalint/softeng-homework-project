package foxgame.util;

import foxgame.model.Position;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStateTest {

    @Test
    void testToString() {
        ArrayList<TwoPhaseMoveState.TwoPhaseMove<Position>> moves = new ArrayList<>();
        moves.add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 2), new Position(3, 4)));
        moves.add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(5, 6), new Position(7, 8)));
        GameState gameState = new GameState("test", "one", "two", moves);
        assertEquals("name:test, playerOne:one, playerTwo:two, moves:[TwoPhaseMove[from=(1,2), to=(3,4)], TwoPhaseMove[from=(5,6), to=(7,8)]]", gameState.toString());
    }
}