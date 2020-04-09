import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class TCPServer {
    private static int serverPort = 8080;
    private static ArrayList<Student> studentList = new ArrayList();
    private static ArrayList<LogEntry> logList = new ArrayList();
    private static String studentFileName = "studententry.txt";
    private static String logFileName = "logentry.txt";

    public static void main(String[] args) {
        System.out.println("TCP Server Running...");

        // Load data from the text files
        try {
            loadStudentsFromFile();
            loadLogEntriesFromFile();
            System.out.println("Text files loaded...");
        } catch (IOException e) {
            System.out.println("An error occurred while loading the files...");
        }

        // Creates a timer to update the text files every 3 minutes.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Collections.sort(logList);
                new WriteToFile(studentList, logList);
            }
        }, 0, 180000);

        // Open the socket for the server.
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            // Listen for connections indefinitely.
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Create a new thread for every connections made.
                new Thread(new Connection(clientSocket, studentList, logList)).start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        System.out.println("Server is closed...");
    }

    /**
     * Used to load student saved within the text file.
     *
     * @throws IOException
     */
    private static void loadStudentsFromFile() throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(studentFileName));
            String nextLine;

            // Read each line of the text file.
            while ((nextLine = bufferedReader.readLine()) != null) {
                // Split the line by spaces.
                String[] studentDetails = nextLine.split(" ");
                studentList.add(new Student(studentDetails[0], Integer.parseInt(studentDetails[1])));
            }
        } catch (FileNotFoundException fileNotFoundException) {
            // Create a new file if the file is not found.
            System.out.println("File not found. Creating a new file named " + studentFileName);
            File file = new File(studentFileName);
            file.createNewFile();
        }
    }

    /**
     * Used to load log entries saved within the text file.
     *
     * @throws IOException
     */
    private static void loadLogEntriesFromFile() throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(logFileName));
            String nextLine;

            // Read each line of the text file.
            while ((nextLine = bufferedReader.readLine()) != null) {
                // Split the line by spaces.
                String[] logEntryDetails = nextLine.split(" ");
                logList.add(new LogEntry(logEntryDetails[0], logEntryDetails[1]));
            }
        } catch (FileNotFoundException fileNotFoundException) {
            // Create a new file if the file is not found.
            System.out.println("File not found. Creating a new file named " + logFileName);
            File file = new File(logFileName);
            file.createNewFile();
        }
    }
}
