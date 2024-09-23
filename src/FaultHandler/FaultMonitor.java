package FaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import Utils.HeartbeatConstants;

public class FaultMonitor {

    public static void startFaultHandler() {
        try (ServerSocket faultHandlerSocket = new ServerSocket(HeartbeatConstants.FAULT_HANDLER_PORT)) {
            System.out.println("Fault handler waiting for failure notifications on port " + HeartbeatConstants.FAULT_HANDLER_PORT);

            while (true) {
                Socket socket = faultHandlerSocket.accept(); 
                Scanner input = new Scanner(socket.getInputStream());

                // Log the failure to the file
                if (input.hasNextLine()) {
                    String failureMessage = input.nextLine();
                    logFailure(failureMessage); 
                }

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log the device failure to the log file
    private static void logFailure(String failureMessage) {
        try (FileWriter fw = new FileWriter(HeartbeatConstants.LOG_FILE, true)) {  // true to append to file
            fw.write(failureMessage + " at: " + new Date() + "\n");
            System.out.println("Failure logged to: " + HeartbeatConstants.LOG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}