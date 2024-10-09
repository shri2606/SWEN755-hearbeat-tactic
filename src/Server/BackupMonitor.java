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

    public static void initializeBackupServer() {
        try (ServerSocket backupSocket = new ServerSocket(HeartbeatConstants.BACKUP_PORT)) {
            System.out.println("Backup monitoring system standing by on port " + HeartbeatConstants.BACKUP_PORT);

            while (true) {
                Socket clientSocket = backupSocket.accept(); // Accept connection from wearable
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Restore the checkpoint from the file (checkpoint.txt)
                CheckpointManager.CheckpointData checkpoint = CheckpointManager.loadCheckpoint();
                long lastPulseTime = checkpoint.getLastPulseTime();
                int heartbeatSeq = checkpoint.getHeartbeatSequence();

                
                System.out.println("Restored last pulse time: " + lastPulseTime);
                System.out.println("Resuming heartbeat monitoring from sequence number: " + heartbeatSeq);

                // Backup monitor starts monitoring
                while (true) {
                    if (br.ready()) {
                        String heartbeat = br.readLine();
                        lastPulseTime = System.currentTimeMillis(); // Reset the pulse time
                        System.out.println("Received heartbeat by backup monitor: " + heartbeat);

                        // Increment and print the next sequence number
                        heartbeatSeq++;
                        //System.out.println("Checkpoint updated. Sequence number: " + heartbeatSeq);

                        // Update the checkpoint
                        CheckpointManager.saveCheckpoint(heartbeatSeq, lastPulseTime);
                    }

                    // Check for heartbeat failure
                    if ((System.currentTimeMillis() - lastPulseTime) > HeartbeatConstants.PULSE_TIMEOUT) {
                        logger.warning("No heartbeat received within " + HeartbeatConstants.PULSE_TIMEOUT + " ms. Backup monitor detecting failure.");
                        CheckpointManager.saveCheckpoint(heartbeatSeq, lastPulseTime);
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
