
public class TicketGenerator {

    static volatile long result;

    static long usingString(int tickets, int fields) {
        long total = 0;

        for (int t = 0; t < tickets; t++) {
            String ticket = "";

            for (int i = 1; i <= fields; i++) {
                ticket = ticket + "Field" + i + " ";
            }

            total += ticket.length();
        }
        return total;
    }

    static long usingStringBuilder(int tickets, int fields) {
        long total = 0;

        for (int t = 0; t < tickets; t++) {
            StringBuilder ticket = new StringBuilder();

            for (int i = 1; i <= fields; i++) {
                ticket.append("Field").append(i).append(" ");
            }

            total += ticket.length();
        }
        return total;
    }

    static long usingStringBuffer(int tickets, int fields) {
        long total = 0;

        for (int t = 0; t < tickets; t++) {
            StringBuffer ticket = new StringBuffer();

            for (int i = 1; i <= fields; i++) {
                ticket.append("Field").append(i).append(" ");
            }

            total += ticket.length();
        }
        return total;
    }

    static void test(String workload, int tickets, int fields) {

        System.out.println("\n" + workload);
        System.out.println("Tickets: " + tickets);
        System.out.println("Fields per ticket: " + fields);

        // Warm-up
        result = usingString(2, 5);
        result = usingStringBuilder(2, 5);
        result = usingStringBuffer(2, 5);

        int repeats = 5;

        for (int method = 1; method <= 3; method++) {

            long totalTime = 0;

            for (int r = 0; r < repeats; r++) {

                long start = System.nanoTime();
                long answer = 0;

                if (method == 1)
                    answer = usingString(tickets, fields);

                else if (method == 2)
                    answer = usingStringBuilder(tickets, fields);

                else
                    answer = usingStringBuffer(tickets, fields);

                long end = System.nanoTime();

                result = answer;
                totalTime += (end - start);
            }

            double average = totalTime / (double) repeats;

            String name = method == 1 ? "String" :
                    method == 2 ? "StringBuilder" : "StringBuffer";

            System.out.printf("%s Average: %.2f ms%n",
                    name, average / 1_000_000);
        }
    }

    public static void main(String[] args) {

        test("WORKLOAD 1 - Small", 1, 6);

        test("WORKLOAD 2 - Medium", 1000, 10);

        test("WORKLOAD 3 - Large", 10000, 50);
    }
}
