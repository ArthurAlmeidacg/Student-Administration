package Interface;

import java.util.List;

public interface PVL2 {
    int enroll_student (String first_name , String surname);
    boolean disenroll_student (int number);
    void take_exam (int number , String courseID , float grade);
    List<String> get_student(int number);

    
}
