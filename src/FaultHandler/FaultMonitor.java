package FaultHandler;

import Utils.HeartbeatConstants;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class FaultMonitor {

    public static void startFaultHandler() {
        try (ServerSocket errorSocketHandler = new ServerSocket(HeartbeatConstants.ERROR_HANDLER_PORT)) {
            System.out.println("Fault handler waiting for failure notifications on port " + HeartbeatConstants.ERROR_HANDLER_PORT);

            while (true) {
                Socket socket = errorSocketHandler.accept(); 
                Scanner input = new Scanner(socket.getInputStream());

                // Log the failure to the file
                if (input.hasNextLine()) {
                    String errorMessage = input.nextLine();
                    logFailure(errorMessage); 
                }

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log the device failure to the log file
    private static void logFailure(String errorMessage) {
        try (FileWriter fw = new FileWriter(HeartbeatConstants.DEBUG_LOG, true)) {  // true to append to file
            fw.write(errorMessage + " at: " + new Date() + "\n");
            System.out.println("Failure logged to: " + HeartbeatConstants.DEBUG_LOG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
