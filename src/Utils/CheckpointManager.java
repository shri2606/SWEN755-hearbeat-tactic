package Utils;

import java.io.*;
import java.util.logging.Logger;

public class CheckpointManager {
    private static final String CHECKPOINT_PATH = "./src/Logger/checkpoint.txt";
    private static final Logger logger = Logger.getLogger(CheckpointManager.class.getName());

    // CheckpointData class to hold the checkpoint information
    public static class CheckpointData {
        private final int seqID;
        private final long prevPulseTime;

        public CheckpointData(int seqID, long prevPulseTime) {
            this.seqID = seqID;
            this.prevPulseTime = prevPulseTime;
        }

        public int getSeqID() {
            return seqID;
        }

        public long getLastPulseTime() {
            return prevPulseTime;
        }
    }

    // Save the checkpoint data to a file
    public static void writeCheckpoint(int seqID, long prevPulseTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHECKPOINT_PATH))) {
            writer.write(seqID + "\n");
            writer.write(Long.toString(prevPulseTime) + "\n");
            writer.flush();
        } catch (IOException e) {
            logger.severe("Failed to save checkpoint: " + e.getMessage());
        }
    }

    // Load the checkpoint data from the file
    public static CheckpointData fetchCheckpoint() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CHECKPOINT_PATH))) {
            int seqID = Integer.parseInt(reader.readLine());
            long prevPulseTime = Long.parseLong(reader.readLine());
            return new CheckpointData(seqID, prevPulseTime);
        } catch (IOException | NumberFormatException e) {
            logger.warning("Failed to load checkpoint, initializing default values.");
            return new CheckpointData(1, System.currentTimeMillis());
        }
    }
}
