package FaultHandler;

import Utils.HeartbeatConstants;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class FaultMonitor {

    private static boolean failureDetected = false;  // To track if a failure has been detected

    public static void launchFaultHandler() {
        try (ServerSocket errorSocketHandler = new ServerSocket(HeartbeatConstants.ERROR_MANAGER_PORT)) {
            System.out.println("Fault handler waiting for failure notifications on port " + HeartbeatConstants.ERROR_MANAGER_PORT);

            while (true) {
                Socket socket = errorSocketHandler.accept(); 
                Scanner input = new Scanner(socket.getInputStream());

                // Log the failure to the file
                if (input.hasNextLine()) {
                    String failureMessage = input.nextLine();
                    recordFailure(failureMessage); 
                    failureDetected = true;  // Mark that a failure has occurred
                }

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log the device failure to the log file
    private static void recordFailure(String failureMessage) {
        try (FileWriter fw = new FileWriter(HeartbeatConstants.TRACE_LOG, true)) {  // true to append to file
            fw.write(failureMessage + " at: " + new Date() + "\n");
            System.out.println("Failure logged to: " + HeartbeatConstants.TRACE_LOG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for the backup monitor to check if a failure has occurred
    public static boolean containsFailure() {
        return failureDetected;
    }

    // Reset failure state after backup takes over
    public static void resetFailure() {
        failureDetected = false;
    }
}
