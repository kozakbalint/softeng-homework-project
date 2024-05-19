package foxgame.util;

public final class PlayerStats {
    private final String name;
    private final Long wins;

    public PlayerStats(String name, Long wins) {
        this.name = name;
        this.wins = wins;
    }

    public String name() {
        return name;
    }

    public Long wins() {
        return wins;
    }
}
