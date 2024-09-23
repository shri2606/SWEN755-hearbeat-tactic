package Server;

import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.logging.Logger;
import Utils.HeartbeatConstants;

public class HeartbeatMonitor {

    private static final Logger logger = Logger.getLogger(HeartbeatMonitor.class.getName());

    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(HeartbeatConstants.SERVER_PORT)) {
            System.out.println("Monitoring system waiting for connection on port " + HeartbeatConstants.SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept connection from wearable
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                long lastHeartbeatTime = System.currentTimeMillis();

                // Start monitoring the connected wearable device
                while (true) {
                    if (in.ready()) {
                        String heartbeat = in.readLine();
                        System.out.println("Received: " + heartbeat);
                        lastHeartbeatTime = System.currentTimeMillis(); // Reset the heartbeat time
                    }

                    // Check if heartbeat timeout is exceeded
                    if ((System.currentTimeMillis() - lastHeartbeatTime) > HeartbeatConstants.HEARTBEAT_TIMEOUT) {
                        logger.warning("No heartbeat received within " + HeartbeatConstants.HEARTBEAT_TIMEOUT + " ms. Device failure detected.");
                        notifyFaultHandler("Device failure detected"); // Notify fault handler
                        break; // Stop monitoring this device after failure
                    }

                    Thread.sleep(100); // Sleep for a short time before checking again
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Notify the fault handler about the failure
    private static void notifyFaultHandler(String failureMessage) {
        try (Socket socket = new Socket(HeartbeatConstants.FAULT_HANDLER_HOST, HeartbeatConstants.FAULT_HANDLER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(failureMessage); // Send failure message to fault handler
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
