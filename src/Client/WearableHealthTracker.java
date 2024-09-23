package Client;

import Utils.HeartbeatConstants;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class WearableHealthTracker {

    public static void connectClient() {
        try {
            // Connect to the monitoring server
            Socket socket = new Socket(HeartbeatConstants.BACKEND_HOST, HeartbeatConstants.BACKEND_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            
            Random random = new Random();

            while (true) {
                // Simulate random failure with a certain probability
                if (random.nextDouble() < HeartbeatConstants.RISK_OF_FAILURE) {
                    System.out.println("Wearable device: Simulating random failure...");
                    break; 
                }

                // Send heartbeat
                System.out.println("Heartbeat from wearable device");
                System.out.println("Wearable device: Heartbeat sent");

                // Waiting for next heartbeat interval
                Thread.sleep(HeartbeatConstants.PULSE_INTERVAL);
            }

            // Closing the socket to simulate the device stopping
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
