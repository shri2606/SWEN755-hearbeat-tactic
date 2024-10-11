package Server;

import Utils.HeartbeatConstants;
import Utils.CheckpointManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class HeartbeatMonitor {
    private static final Logger logger = Logger.getLogger(HeartbeatMonitor.class.getName());

    public static void initializeServer() {
        try (ServerSocket serverSocket = new ServerSocket(HeartbeatConstants.SERVER_PORT)) {
            System.out.println("Primary monitoring system waiting for connection on port " + HeartbeatConstants.SERVER_PORT);

            while (true) {
                Socket clientConnection = serverSocket.accept(); // Accept connection from wearable
                BufferedReader br = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));

                long prevPulseTime = System.currentTimeMillis();
                int seqID = 0; // Initial sequence number

                // Start monitoring the wearable device
                while (true) {
                    if (br.ready()) {
                        String pulse = br.readLine();
                        prevPulseTime = System.currentTimeMillis(); // Reset the pulse time
                        seqID++; // Increment the sequence number
                        System.out.println("Received: Heartbeat #" + seqID + " sent at " + prevPulseTime);
                    }

                    // Check if heartbeat timeout is exceeded
                    if ((System.currentTimeMillis() - prevPulseTime) > HeartbeatConstants.HEARTBEAT_TIMEOUT) {
                        logger.warning("No heartbeat received within " + HeartbeatConstants.HEARTBEAT_TIMEOUT + " ms. Device failure detected.");
                        triggerFaultHandler("Device failure detected"); // Notify fault handler
                        CheckpointManager.writeCheckpoint(seqID, prevPulseTime); // Ensure the order of arguments
                        System.out.println("Failure logged. Heartbeat sequence: " + seqID);
                        System.out.println("Primary monitor failure detected. Backup monitor taking over...");
                        break; // Stop monitoring this device after failure
                    }

                    Thread.sleep(100); // Sleep for a short time before checking again
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void triggerFaultHandler(String failureMessage) {
        try (Socket socket = new Socket(HeartbeatConstants.ERROR_MANAGER_HOST, HeartbeatConstants.ERROR_MANAGER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(failureMessage); // Send failure message to fault handler
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
