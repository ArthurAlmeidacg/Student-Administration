package ArthurAlmeidaAlves;

import Interface.PVL2;

import java.util.ArrayList;
import java.util.List;

public class StudentManagement implements PVL2 {

    private static List<StudentManagement> students = new ArrayList<StudentManagement>();
    private String name;
    private String vorname;
    private int matrikelNummer;
    private static int nextId = 1;
    private List<List<String>> courses;

    public StudentManagement(String vorname, String name) {
        this.name = name;
        this.vorname = vorname;
        this.matrikelNummer = getNextId();
        courses = new ArrayList<List<String>>();
    }

    public void addCourse(String studentId, String courseName, String grade, String attempts) {
        List<String> course = new ArrayList<String>();

        course.add(studentId);
        course.add(courseName);
        course.add(grade);
        course.add(attempts);
        courses.add(course);
    }

    public static int getNextId() {
        int id = nextId;
        if (nextId < 999) {
            nextId++;
        } else {
            nextId = 1;
        }
        return id;
    }

    @Override
    public int enroll_student(String first_name, String surname) {

        StudentManagement newStudent = new StudentManagement(first_name, surname);

        for (StudentManagement student : students) {
            if (student.getFirstName().equals(first_name) && student.getLastName().equals(surname)) {
                return -1;
            }
        }

        students.add(newStudent);
        return newStudent.getStudentId();
    }

    @Override
    public boolean disenroll_student(int number) {
        for (StudentManagement student : students) {
            if (student.getStudentId() == number) {
                students.remove(student);
                return true;
            }
        }
        return false;
    }

    @Override
    public void take_exam(int number, String courseID, float grade) {

        StudentManagement student = null;
        for (StudentManagement s : students) {
            if (s.getStudentId() == number) {
                student = s;
                break;
            }
        }
        if (student == null) {
            System.out.println("Error: student with number " + number + " not found");
            return;
        }

        boolean courseExists = false;
        for (List<String> course : student.getCourses()) {
            if (course.get(1).equals(courseID)) {
                courseExists = true;

                if (Float.parseFloat(course.get(2)) < 5.0) {

                    course.set(2, String.valueOf(grade));

                    if (Integer.parseInt(course.get(3)) > 2 && grade < 5.0) {
                        System.out.println("Student " + student.getStudentId() + " has failed course " + courseID
                                + " three times and will be expelled.");
                        students.remove(student);
                    }
                }
                break;
            }
        }

        if (!courseExists) {
            List<String> course = new ArrayList<String>();
            course.add(String.valueOf(student.getStudentId()));
            course.add(courseID);
            course.add(String.valueOf(grade));
            course.add("1");
            student.getCourses().add(course);
        }
    }

    @Override
    public List<String> get_student(int number) {
        for (StudentManagement student : students) {
            if (student.getStudentId() == number) {
                List<String> studentInfo = new ArrayList<String>();
                studentInfo.add(student.getFirstName());
                studentInfo.add(student.getLastName());
                studentInfo.add(Integer.toString(student.getStudentId()));

                for (List<String> course : student.getCourses()) {
                    String courseInfo = course.get(1) + " " + course.get(2) + " " + course.get(3);
                    studentInfo.add(courseInfo);
                }

                return studentInfo;
            }
        }
        return null;
    }

    public String getFirstName() {
        return vorname;
    }

    public String getLastName() {
        return name;
    }

    public int getStudentId() {
        return matrikelNummer;
    }

    public List<List<String>> getCourses() {
        return courses;
    }

    public static void main(String[] args) {
        StudentManagement studentManagement = new StudentManagement("John", "Doe");
        int studentId = studentManagement.enroll_student("Alice", "Smith");
        if (studentId == -1) {
            System.out.println("Student already exists");
        } else {
            System.out.println("Enrolled student with ID " + studentId);
        }
        studentManagement.addCourse(Integer.toString(studentId), "Math", "5.0", "1");
        studentManagement.take_exam(studentId, "Math", 4.0f);
        List<String> studentInfo = studentManagement.get_student(studentId);
        System.out.println("Student information:");
        for (String info : studentInfo) {
            System.out.println(info);
        }
        boolean success = studentManagement.disenroll_student(studentId);
        if (success) {
            System.out.println("Student disenrolled");
        } else {
            System.out.println("Could not disenroll student");
        }
    }

}
