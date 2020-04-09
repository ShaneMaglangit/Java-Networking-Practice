import java.io.Serializable;

public class Student implements Serializable {
    private String studentNumber;
    private int pinCode;

    public Student() {
    }

    public Student(String studentNumber, int pinCode) {
        this.studentNumber = studentNumber;
        this.pinCode = pinCode;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public String toString() {
        return String.format("%s %d", studentNumber, pinCode);
    }
}
