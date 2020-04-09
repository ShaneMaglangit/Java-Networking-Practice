import java.io.Serializable;

public class LogEntry implements Serializable, Comparable {
    private String studentNumber;
    private int pinCode;
    private String dateTime;

    public LogEntry() {
    }

    public LogEntry(String studentNumber, String dateTime) {
        this.studentNumber = studentNumber;
        this.dateTime = dateTime;
    }

    public LogEntry(String studentNumber, int pinCode, String dateTime) {
        this.studentNumber = studentNumber;
        this.pinCode = pinCode;
        this.dateTime = dateTime;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(Object o) {
        LogEntry otherObject = (LogEntry) o;
        return this.getStudentNumber().compareTo(otherObject.getStudentNumber());
    }

    @Override
    public String toString() {
        return String.format("%s %s", studentNumber, dateTime);
    }
}
