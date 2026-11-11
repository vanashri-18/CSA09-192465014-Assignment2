
import java.util.*;
import java.util.function.*;

public class PlaylistBenchmark {

    static volatile long result;

    static List<Integer> createList(String type, int size) {
        List<Integer> list;

        if (type.equals("ArrayList")) {
            list = new ArrayList<>();
        } else {
            list = new LinkedList<>();
        }

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    static <T> void measure(
            String operation,
            Supplier<T> setup,
            Consumer<T> task) {

        int repeats = 5;
        long totalTime = 0;

        // Warm-up
        task.accept(setup.get());

        for (int i = 0; i < repeats; i++) {

            T data = setup.get();

            long start = System.nanoTime();

            task.accept(data);

            long end = System.nanoTime();

            totalTime += end - start;
        }

        double average =
                totalTime / (double) repeats / 1_000_000;

        System.out.printf("%-25s %.3f ms%n",
                operation, average);
    }

    static void testList(String type, int size) {

        System.out.println("\n" + type +
                " - Playlist Size: " + size);

        measure("Add at end",
                () -> createList(type, size),
                list -> {
                    for (int i = 0; i < 1000; i++) {
                        list.add(i);
                    }
                    result = list.size();
                });

        measure("Insert at front",
                () -> createList(type, size),
                list -> {
                    for (int i = 0; i < 100; i++) {
                        list.add(0, i);
                    }
                    result = list.size();
                });

        measure("Insert in middle",
                () -> createList(type, size),
                list -> {
                    for (int i = 0; i < 100; i++) {
                        list.add(list.size() / 2, i);
                    }
                    result = list.size();
                });

        measure("Remove from front",
                () -> createList(type, size),
                list -> {
                    for (int i = 0; i < 100; i++) {
                        list.remove(0);
                    }
                    result = list.size();
                });

        measure("Remove from middle",
                () -> createList(type, size),
                list -> {
                    for (int i = 0; i < 100; i++) {
                        list.remove(list.size() / 2);
                    }
                    result = list.size();
                });

        measure("Access k-th song",
                () -> createList(type, size),
                list -> {
                    long sum = 0;

                    for (int i = 0; i < 1000; i++) {
                        sum += list.get(size / 2);
                    }

                    result = sum;
                });

        measure("Iterate playlist",
                () -> createList(type, size),
                list -> {
                    long sum = 0;

                    for (int song : list) {
                        sum += song;
                    }

                    result = sum;
                });
    }

    static void testArrayDeque(int size) {

        System.out.println(
                "\nArrayDeque - Up Next Queue Size: " + size);

        measure("Add at both ends",
                () -> {
                    ArrayDeque<Integer> queue =
                            new ArrayDeque<>();

                    for (int i = 0; i < size; i++) {
                        queue.addLast(i);
                    }

                    return queue;
                },
                queue -> {
                    for (int i = 0; i < 1000; i++) {
                        queue.addFirst(i);
                        queue.addLast(i);
                    }

                    result = queue.size();
                });

        measure("Remove from front",
                () -> {
                    ArrayDeque<Integer> queue =
                            new ArrayDeque<>();

                    for (int i = 0; i < size; i++) {
                        queue.addLast(i);
                    }

                    return queue;
                },
                queue -> {
                    for (int i = 0; i < 100; i++) {
                        queue.removeFirst();
                    }

                    result = queue.size();
                });
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        for (int size : sizes) {

            System.out.println("\n==============================");
            System.out.println("PLAYLIST SIZE: " + size);
            System.out.println("==============================");

            testList("ArrayList", size);
            testList("LinkedList", size);
            testArrayDeque(size);
        }
    }
}
