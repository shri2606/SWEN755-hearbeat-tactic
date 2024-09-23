package Server;

import Utils.HeartbeatConstants;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class HeartbeatMonitor {

    private static final Logger logger = Logger.getLogger(HeartbeatMonitor.class.getName());

    public static void initializeServer() {
        try (ServerSocket backendSocket = new ServerSocket(HeartbeatConstants.BACKEND_PORT)) {
            System.out.println("Monitoring system waiting for connection on port " + HeartbeatConstants.BACKEND_PORT);

            while (true) {
                Socket userSocket = backendSocket.accept(); // Accept connection from wearable
                BufferedReader br = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));

                long lastPulseTime = System.currentTimeMillis();

                // Start monitoring the connected wearable device
                while (true) {
                    if (br.ready()) {
                        String heartbeat = br.readLine();
                        System.out.println("Received: " + heartbeat);
                        lastPulseTime = System.currentTimeMillis(); // Reset the heartbeat time
                    }

                    // Check if heartbeat timeout is exceeded
                    if ((System.currentTimeMillis() - lastPulseTime) > HeartbeatConstants.PULSE_TIMEOUT) {
                        logger.warning("No heartbeat received within " + HeartbeatConstants.PULSE_TIMEOUT + " ms. Device failure detected.");
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
        try (Socket socket = new Socket(HeartbeatConstants.ERROR_HANDLER_HOST, HeartbeatConstants.ERROR_HANDLER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(failureMessage); // Send failure message to fault handler
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
