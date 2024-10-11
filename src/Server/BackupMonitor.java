package Server;

import Utils.HeartbeatConstants;
import Utils.CheckpointManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class BackupMonitor {
    private static final Logger logger = Logger.getLogger(BackupMonitor.class.getName());

    public static void activateBackupServer() {
        try (ServerSocket failoverSocket = new ServerSocket(HeartbeatConstants.FALLBACK_PORT)) {
            System.out.println("Backup monitoring system standing by on port " + HeartbeatConstants.FALLBACK_PORT);

            while (true) {
                Socket clientConnection = failoverSocket.accept(); // Accept connection from wearable
                BufferedReader br = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));

                // Restore the checkpoint from the file (checkpoint.txt)
                CheckpointManager.CheckpointData checkpoint = CheckpointManager.fetchCheckpoint();
                long prevPulseTime = checkpoint.getLastPulseTime();
                int seqID = checkpoint.getSeqID();

                
                System.out.println("Restored last pulse time: " + prevPulseTime);
                System.out.println("Resuming heartbeat monitoring from sequence number: " + seqID);

                // Backup monitor starts monitoring
                while (true) {
                    if (br.ready()) {
                        String pulse = br.readLine();
                        prevPulseTime = System.currentTimeMillis(); // Reset the pulse time
                        System.out.println("Received heartbeat by backup monitor: " + pulse);

                        // Increment and print the next sequence number
                        seqID++;
                        //System.out.println("Checkpoint updated. Sequence number: " + seqID);

                        // Update the checkpoint
                        CheckpointManager.writeCheckpoint(seqID, prevPulseTime);
                    }

                    // Check for heartbeat failure
                    if ((System.currentTimeMillis() - prevPulseTime) > HeartbeatConstants.HEARTBEAT_TIMEOUT) {
                        logger.warning("No heartbeat received within " + HeartbeatConstants.HEARTBEAT_TIMEOUT + " ms. Backup monitor detecting failure.");
                        CheckpointManager.writeCheckpoint(seqID, prevPulseTime);
                        System.out.println("Backup monitor has finished handling the failure.");
                        
                        // Stop the program here since the backup monitor failed as well
                        System.out.println("Exiting the system due to backup monitor failure.");
                        System.exit(0);
                    }

                    Thread.sleep(100); // Sleep briefly before checking again
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
