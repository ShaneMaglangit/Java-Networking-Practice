import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class TCPClient {
    private static int portNumber = 8080;
    private static String serverName = "localhost";

    public static void main(String[] args) {
        // Connect to the server.
        try(Socket socket = new Socket(serverName, portNumber)) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            mainLoop:
            while (true) {
                String serverResponse;
                int action = promptUserSelection(scanner);

                outputStream.writeInt(action);

                // Ask for the student details if the user is current student or would like to create an account.
                if(action == 1 || action == 2) {
                    Student student = promptStudentDetails(scanner);
                    LogEntry logEntry = createLogEntry(student);

                    // Write the selection, student, and logEntry to the output stream.
                    outputStream.writeObject(student);
                    outputStream.writeObject(logEntry);
                    outputStream.flush();
                }

                outputStream.flush();

                // Wait for the server response.
                serverResponse = (String) inputStream.readObject();
                showSystemResponse(serverResponse);

                // Stop the loop and leave the program if the user decides to exit.
                if(action == 3) break mainLoop;
            }

            outputStream.close();
            inputStream.close();
            scanner.close();
        } catch (ClassNotFoundException | IOException unknownHostException) {
            System.out.println("An error occurred while trying to connect to the server. Please try again.");
        }
    }

    /**
     * Used to create a log entry.
     * @param student object to get the student details from.
     * @return the log entry created.
     */
    private static LogEntry createLogEntry(Student student) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        String dateTime = formatter.format(new Date());
        LogEntry logEntry = new LogEntry(student.getStudentNumber(), student.getPinCode(), dateTime);
        return logEntry;
    }

    /**
     * Used to ask for the desired action.
     * @param scanner
     * @return index of the selected action.
     */
    private static int promptUserSelection(Scanner scanner) {
        System.out.println("PLEASE MAKE YOUR SELECTION");
        System.out.println("*******************");
        System.out.println("1. Current Student");
        System.out.println("2. New Student");
        System.out.println("3. Exit");
        System.out.println("*******************");
        System.out.println("Enter your option:");

        try{
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException numberFormatException) {
            showSystemResponse("Invalid Entry..try again");
            return promptUserSelection(scanner);
        }
    }

    /**
     * Used to ask for the student account details.
     * @param scanner
     * @return student
     */
    private static Student promptStudentDetails(Scanner scanner) {
        Student student = new Student();
        String studentName;
        String pinCode;

        System.out.println("Enter Student Number:");
        studentName = scanner.nextLine();
        System.out.println("Enter 4 Digit Pin Code:");
        pinCode = scanner.nextLine();

        student.setStudentNumber(studentName);

        try {
            student.setPinCode(Integer.valueOf(pinCode));
        } catch (NumberFormatException numberFormatException) {
            student.setPinCode(0);
        }

        return student;
    }

    /**
     * Used to show a message with text decorations.
     * @param message message to be shown.
     */
    private static void showSystemResponse(String message) {
        System.out.printf("********** %s **********\n\n", message);
    }
}
