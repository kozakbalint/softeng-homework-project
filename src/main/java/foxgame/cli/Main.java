package foxgame.cli;

import foxgame.model.FoxGameState;
import foxgame.model.Position;
import game.console.TwoPhaseMoveGame;

import java.util.Scanner;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        FoxGameState gameState = new FoxGameState();

        TwoPhaseMoveGame<Position> cli = new TwoPhaseMoveGame<>(gameState, Main::parsePosition);
        System.out.println("Usage: <row> <col>");
        System.out.println("Example: 0 2");
        cli.start();
    }

    public static Position parsePosition(String s) {
        s = s.trim();
        if (!s.matches("\\d+\\s+\\d+")) {
            throw new IllegalArgumentException();
        }
        var scanner = new Scanner(s);
        return new Position(scanner.nextInt(), scanner.nextInt());
    }
}
