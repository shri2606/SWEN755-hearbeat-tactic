package Utils;

import java.io.*;
import java.util.logging.Logger;

public class CheckpointManager {
    private static final String CHECKPOINT_FILE = "./src/Logger/checkpoint.txt";
    private static final Logger logger = Logger.getLogger(CheckpointManager.class.getName());

    // CheckpointData class to hold the checkpoint information
    public static class CheckpointData {
        private final int heartbeatSequence;
        private final long lastPulseTime;

        public CheckpointData(int heartbeatSequence, long lastPulseTime) {
            this.heartbeatSequence = heartbeatSequence;
            this.lastPulseTime = lastPulseTime;
        }

        public int getHeartbeatSequence() {
            return heartbeatSequence;
        }

        public long getLastPulseTime() {
            return lastPulseTime;
        }
    }

    // Save the checkpoint data to a file
    public static void saveCheckpoint(int heartbeatSequence, long lastPulseTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHECKPOINT_FILE))) {
            writer.write(heartbeatSequence + "\n");
            writer.write(Long.toString(lastPulseTime) + "\n");
            writer.flush();
        } catch (IOException e) {
            logger.severe("Failed to save checkpoint: " + e.getMessage());
        }
    }

    // Load the checkpoint data from the file
    public static CheckpointData loadCheckpoint() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CHECKPOINT_FILE))) {
            int heartbeatSequence = Integer.parseInt(reader.readLine());
            long lastPulseTime = Long.parseLong(reader.readLine());
            return new CheckpointData(heartbeatSequence, lastPulseTime);
        } catch (IOException | NumberFormatException e) {
            logger.warning("Failed to load checkpoint, initializing default values.");
            return new CheckpointData(1, System.currentTimeMillis());
        }
    }
}
