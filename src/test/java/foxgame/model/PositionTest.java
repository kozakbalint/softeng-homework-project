package foxgame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    @Test
    void testToString() {
        Position position = new Position(1, 2);
        assertEquals("(1,2)", position.toString());
    }
}