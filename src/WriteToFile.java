import java.io.*;
import java.util.*;

public class WriteToFile {
    private PrintWriter pm;
    private ArrayList<Student> studentList;
    private ArrayList<LogEntry> logList;
    private String studentFileName;
    private String logFileName;

    public WriteToFile(ArrayList<Student> studentList, ArrayList<LogEntry> logList) {
        this.studentList = studentList;
        this.logList = logList;
        this.studentFileName = "studententry.txt";
        this.logFileName = "logentry.txt";

        writeStudents();
        writeLogEntry();
    }

    /**
     * Used to write the content of the student list to the text file.
     */
    private void writeStudents() {
        try {
            pm = new PrintWriter(new File(studentFileName));
            for(Student student : studentList) {
                pm.println(student);
            }
            pm.close();
        } catch (IOException e) {
            System.out.println("An error occcurred while trying to write to " + studentFileName);
        }
    }

    /**
     * Used to write the content of the log list to the text file.
     */
    private void writeLogEntry() {
        try {
            pm = new PrintWriter(new File(logFileName));
            for (LogEntry logEntry : logList) {
                pm.println(logEntry);
            }
            pm.close();
        } catch (IOException e) {
            System.out.println("An error occcurred while trying to write to " + logFileName);
        }
    }
}
