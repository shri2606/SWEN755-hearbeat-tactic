package Client;

import Utils.HeartbeatConstants;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class WearableHealthTracker {

    public static void startClient() {
        try {
            // Connect to the monitoring server (Primary or Backup)
            Socket socket = new Socket(HeartbeatConstants.SERVER_HOST, HeartbeatConstants.SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Random random = new Random();
            int seqID = 1; // Start with heartbeat 1

            while (true) {
                // Simulate random failure with a certain probability
                if (random.nextDouble() < HeartbeatConstants.FAILURE_PROBABILTY) {
                    System.out.println("Wearable device: Simulating random failure...");
                    socket.close();  // Close connection to simulate failure
                    System.out.println("Wearable device: Connection closed after failure.");

                    // Wait for a few seconds before attempting to reconnect
                    Thread.sleep(5000);  // Simulate downtime after failure
                    System.out.println("Wearable device: Attempting to reconnect...");
                    
                    // Reconnect to backup monitor (could be same or different port depending on setup)
                    socket = new Socket(HeartbeatConstants.SERVER_HOST, HeartbeatConstants.FALLBACK_PORT); 
                    out = new PrintWriter(socket.getOutputStream(), true);  // Re-establish output stream
                    System.out.println("Wearable device: Reconnected to backup monitor.");
                }

                // Send heartbeat with the current sequence number
                long pulseTime = System.currentTimeMillis();
                out.println("Heartbeat #" + seqID + " sent at " + pulseTime);
                System.out.println("Wearable device: Heartbeat #" + seqID + " sent");

                // Increment sequence number
                seqID++;

                // Waiting for next heartbeat interval
                Thread.sleep(HeartbeatConstants.HEARTBEAT_INTERVAL);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
