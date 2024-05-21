package foxgame.cli;

import foxgame.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MainTest {

    @Test
    void parsePosition() {
        assertEquals(new Position(1, 2), Main.parsePosition("1 2"));
        assertEquals(new Position(0, 0), Main.parsePosition("0 0"));
        assertEquals(new Position(7, 7), Main.parsePosition("7 7"));
    }

    @Test
    void parsePosition_Throws() {
        assertThrows(IllegalArgumentException.class, () -> Main.parsePosition("1"));
        assertThrows(IllegalArgumentException.class, () -> Main.parsePosition("1 2 3"));
    }
}