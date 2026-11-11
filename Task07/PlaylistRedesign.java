
import java.util.LinkedList;
import java.util.Iterator;

public class PlaylistRedesign {

    static volatile long checksum = 0;

    static long originalMethod(LinkedList<Integer> playlist) {
        long sum = 0;

        for (int i = 0; i < playlist.size(); i++) {
            sum += playlist.get(i);
        }

        checksum = sum;
        return sum;
    }

    static long redesignedMethod(LinkedList<Integer> playlist) {
        long sum = 0;
        Iterator<Integer> iterator = playlist.iterator();

        while (iterator.hasNext()) {
            sum += iterator.next();
        }

        checksum = sum;
        return sum;
    }

    static double measureOriginal(LinkedList<Integer> playlist) {
        long start = System.nanoTime();
        originalMethod(playlist);
        long end = System.nanoTime();

        return (end - start) / 1_000_000.0;
    }

    static double measureRedesigned(LinkedList<Integer> playlist) {
        long start = System.nanoTime();
        redesignedMethod(playlist);
        long end = System.nanoTime();

        return (end - start) / 1_000_000.0;
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        for (int size : sizes) {

            LinkedList<Integer> playlist = new LinkedList<>();

            for (int i = 1; i <= size; i++) {
                playlist.add(i);
            }

            // Warm-up
            originalMethod(playlist);
            redesignedMethod(playlist);

            double originalTotal = 0;
            double redesignedTotal = 0;

            int repetitions = 5;

            for (int i = 0; i < repetitions; i++) {
                originalTotal += measureOriginal(playlist);
                redesignedTotal += measureRedesigned(playlist);
            }

            System.out.println("\nPLAYLIST SIZE: " + size);
            System.out.printf("Original get(i): %.3f ms%n",
                    originalTotal / repetitions);

            System.out.printf("Redesigned Iterator: %.3f ms%n",
                    redesignedTotal / repetitions);

            System.out.println("Results match: " +
                    (originalMethod(playlist) ==
                     redesignedMethod(playlist)));
        }
    }
}
