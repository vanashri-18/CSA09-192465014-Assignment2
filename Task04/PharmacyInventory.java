
import java.util.*;

public class PharmacyInventory {

    static volatile long lookupSink = 0;
    static volatile int reportSink = 0;

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        for (int size : sizes) {

            System.out.println("\nMEDICINE COUNT: " + size);

            Map<String, String> hashMap = new HashMap<>();
            Map<String, String> treeMap = new TreeMap<>();
            Map<String, String> linkedMap = new LinkedHashMap<>();

            for (int i = size; i >= 1; i--) {
                String code = String.format("M%06d", i);
                String medicine = "Medicine" + i;

                hashMap.put(code, medicine);
                treeMap.put(code, medicine);
                linkedMap.put(code, medicine);
            }

            String searchCode = "M000001";

            // Warm-up runs
            for (int i = 0; i < 2; i++) {
                performLookups(hashMap, searchCode);
                performLookups(treeMap, searchCode);
                performLookups(linkedMap, searchCode);
                createCodeReport(hashMap);
                createInsertionReport(linkedMap);
            }

            // Average lookup times
            System.out.println("Average time for 100,000 lookups:");
            System.out.printf("HashMap: %.3f ms%n",
                    measureLookups(hashMap, searchCode));
            System.out.printf("TreeMap: %.3f ms%n",
                    measureLookups(treeMap, searchCode));
            System.out.printf("LinkedHashMap: %.3f ms%n",
                    measureLookups(linkedMap, searchCode));

            // Average report generation times
            System.out.printf("Code-order report: %.3f ms%n",
                    measureCodeReport(hashMap));

            System.out.printf("Insertion-order report: %.3f ms%n",
                    measureInsertionReport(linkedMap));

            System.out.println("Code order correct: "
                    + isSorted(treeMap));

            System.out.println("Insertion order correct: "
                    + isInsertionOrder(linkedMap, size));
        }
    }

    static void performLookups(
            Map<String, String> map, String code) {

        long total = 0;

        for (int i = 0; i < 100000; i++) {
            String value = map.get(code);

            if (value != null) {
                total += value.length();
            }
        }

        lookupSink += total;
    }

    static double measureLookups(
            Map<String, String> map, String code) {

        long totalTime = 0;

        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            performLookups(map, code);
            totalTime += System.nanoTime() - start;
        }

        return totalTime / 5.0 / 1e6;
    }

    static String createCodeReport(Map<String, String> source) {

        Map<String, String> sorted = new TreeMap<>(source);
        StringBuilder report = new StringBuilder();

        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            report.append(entry.getKey()).append(" : ")
                  .append(entry.getValue()).append('\n');
        }

        return report.toString();
    }

    static String createInsertionReport(
            Map<String, String> source) {

        Map<String, String> ordered =
                new LinkedHashMap<>(source);

        StringBuilder report = new StringBuilder();

        for (Map.Entry<String, String> entry : ordered.entrySet()) {
            report.append(entry.getKey()).append(" : ")
                  .append(entry.getValue()).append('\n');
        }

        return report.toString();
    }

    static double measureCodeReport(
            Map<String, String> map) {

        long totalTime = 0;

        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            String report = createCodeReport(map);
            totalTime += System.nanoTime() - start;
            reportSink += report.length();
        }

        return totalTime / 5.0 / 1e6;
    }

    static double measureInsertionReport(
            Map<String, String> map) {

        long totalTime = 0;

        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            String report = createInsertionReport(map);
            totalTime += System.nanoTime() - start;
            reportSink += report.length();
        }

        return totalTime / 5.0 / 1e6;
    }

    static boolean isSorted(Map<String, String> map) {

        String previous = "";

        for (String code : map.keySet()) {
            if (code.compareTo(previous) < 0) {
                return false;
            }

            previous = code;
        }

        return true;
    }

    static boolean isInsertionOrder(
            Map<String, String> map, int size) {

        int expected = size;

        for (String code : map.keySet()) {
            String expectedCode =
                    String.format("M%06d", expected);

            if (!code.equals(expectedCode)) {
                return false;
            }

            expected--;
        }

        return true;
    }
}
