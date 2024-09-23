package Client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import Utils.HeartbeatConstants;

public class WearableHealthTracker {

    public static void startClient() {
        try {
            // Connect to the monitoring server
            Socket socket = new Socket(HeartbeatConstants.SERVER_HOST, HeartbeatConstants.SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            
            Random random = new Random();

            while (true) {
                // Simulate random failure with a certain probability
                if (random.nextDouble() < HeartbeatConstants.FAILURE_PROBABILITY) {
                    System.out.println("Wearable device: Simulating random failure...");
                    break; 
                }

                // Send heartbeat
                out.println("Heartbeat from wearable device");
                System.out.println("Wearable device: Heartbeat sent");

                // Waiting for next heartbeat interval
                Thread.sleep(HeartbeatConstants.HEARTBEAT_INTERVAL);
            }

            // Closing the socket to simulate the device stopping
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
