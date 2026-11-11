
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Team {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {

    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) {
        items.add(item);
    }

    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }

    List<T> ranking() {
        List<T> result = new ArrayList<>(items);
        result.sort(order);
        return result;
    }
}

public class LeaderboardDemo {

    public static void main(String[] args) {

        Comparator<Team> byRank =
                Comparator.comparingInt((Team t) -> t.points)
                .reversed()
                .thenComparing(
                        Comparator.comparingInt(
                                (Team t) -> t.goalDiff).reversed())
                .thenComparing(t -> t.name);

        Leaderboard<Team> board = new Leaderboard<>(byRank);

        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));

        System.out.println("TEAM LEADERBOARD");
        System.out.println(board.ranking());

        System.out.println("\nSECOND TEST - TIE BREAKING");

        board.add(new Team("Tigers", 9, 4));
        board.add(new Team("Wolves", 9, 6));

        System.out.println(board.ranking());
System.out.println("\nFAULTY COMPARATOR TEST");

Comparator<Team> faultyOrder =
        Comparator.comparingInt((Team t) -> t.points).reversed();

List<Team> testTeams = new ArrayList<>();

testTeams.add(new Team("Zebras", 12, 2));
testTeams.add(new Team("Aces", 12, 8));
testTeams.add(new Team("Bears", 12, 8));

testTeams.sort(faultyOrder);

System.out.println(testTeams);
    }
}

