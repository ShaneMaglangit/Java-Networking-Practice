import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Connection implements Runnable{
    private static int activeConnections = 0;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<Student> studentList;
    private ArrayList<LogEntry> logList;

    public Connection(Socket clientSocket, ArrayList<Student> studentList, ArrayList<LogEntry> logList) throws IOException {
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.studentList = studentList;
        this.logList = logList;

        System.out.println("A client has connected to the server. Active connections: " + (++activeConnections));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String response = "";
                int selection = in.readInt();

                switch(selection) {
                    case 1: response = authenticateStudent(); break;
                    case 2: response = registerStudent(); break;
                    case 3: response = "End of Session"; break;
                    default: response = "Invalid Entry..try again";
                }

                out.writeObject(response);
                out.flush();

                if(selection == 3) break;
            }

            out.close();
            in.close();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            // An issue occurred with the connection, disconnecting automatically from the server.
        }

        System.out.println("A client has disconnected from the server. Active connections: " + (--activeConnections));
    }

    /**
     * Used to authentication the student.
     * @return result of the authentication.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private String authenticateStudent() throws ClassNotFoundException, IOException {
        Student student = (Student) in.readObject();
        LogEntry logEntry = (LogEntry) in.readObject();

        if(student.getStudentNumber().isEmpty())  return "Error: Student name cannot be empty";
        else if(String.valueOf(student.getPinCode()).length() != 4) return "Error: Invalid Pin Code";

        // check the student list for anything matching student details
        for(Student currentStudent : studentList) {
            if(currentStudent.getStudentNumber().equals(student.getStudentNumber())) {
                if(currentStudent.getPinCode() == student.getPinCode()) {
                    logList.add(logEntry);
                    System.out.println(logEntry);
                    return "Welcome";
                } else {
                    return "Error: Invalid Pin Code";
                }
            }
        }

        return "Error: Un-registered Student";
    }

    /**
     * Used to register a new student to the system.
     * @return result of the registration.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private String registerStudent() throws ClassNotFoundException, IOException {
        Student student = (Student) in.readObject();
        LogEntry logEntry = (LogEntry) in.readObject();

        if(student.getStudentNumber().isEmpty())  return "Error: Student name cannot be empty";
        else if(String.valueOf(student.getPinCode()).length() != 4) return "Error: Invalid Pin Code";

        // Check if the student number already exists.
        for(Student currentStudent : studentList) {
            if(currentStudent.getStudentNumber().equals(student.getStudentNumber())) {
                return "Error: Student exists";
            }
        }

        System.out.println(logEntry.getStudentNumber() + " " + logEntry.getPinCode());
        studentList.add(student);

        return "Student created";
    }

    /**
     * Used to get the number of current active connections
     * @return size of active connections
     */
    public static int getActiveConnections() {
        return activeConnections;
    }
}
