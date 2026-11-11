
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

class Student {

    String regNo;
    String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Student)) {
            return false;
        }

        Student s = (Student) o;
        return regNo.equals(s.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class Attendance {

    public static void main(String[] args) {

        // Test 1 and Test 2: HashSet
        System.out.println("HASHSET TEST");

        Set<Student> present = new HashSet<>();

        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));
        present.add(new Student("192311001", "Arun"));

        System.out.println("Unique students: " + present.size());

        // Test 3: TreeSet
        System.out.println();
        System.out.println("TREESET TEST");

        Set<Student> tree = new TreeSet<>(
            (a, b) -> a.name.compareTo(b.name)
        );

        tree.add(new Student("192311001", "Arun"));
        tree.add(new Student("192311001", "Arun Kumar"));

        System.out.println("TreeSet size: " + tree.size());

        System.out.println("TreeSet students:");
        for (Student s : tree) {
            System.out.println(s);
        }
    }
}
