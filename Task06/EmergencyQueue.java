
import java.util.PriorityQueue;
import java.util.Comparator;

class Patient {
    String name;
    int severity;
    int arrival;

    Patient(String name, int severity, int arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }
}

public class EmergencyQueue {
    public static void main(String[] args) {

        PriorityQueue<Patient> queue = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                      .thenComparingInt(p -> p.arrival)
        );

        queue.add(new Patient("Arun", 3, 1));
        queue.add(new Patient("Bala", 1, 2));
        queue.add(new Patient("Charan", 2, 3));
        queue.add(new Patient("Divya", 1, 4));
        queue.add(new Patient("Ezhil", 5, 5));
        queue.add(new Patient("Farah", 3, 6));
        queue.add(new Patient("Gokul", 2, 7));
        queue.add(new Patient("Hari", 4, 8));

        System.out.println("EMERGENCY PATIENT TREATMENT ORDER");

        while (!queue.isEmpty()) {
            Patient p = queue.poll();

            System.out.println(
                p.name + " - Severity: " + p.severity
            );
        }
    }
}

