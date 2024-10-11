import Client.WearableHealthTracker;
import FaultHandler.FaultMonitor;
import Server.HeartbeatMonitor;
import Server.BackupMonitor;

public class Main {

    public static void main(String[] args) {
        // Start the fault handler 
        Thread faultDetectionThread = new Thread(() -> FaultMonitor.launchFaultHandler());
        faultDetectionThread.start();

        // Start the primary server 
        Thread serverThread = new Thread(() -> HeartbeatMonitor.initializeServer());
        serverThread.start();

        // Start the backup monitor in standby mode
        Thread backupThread = new Thread(() -> BackupMonitor.activateBackupServer());
        backupThread.start();

        // Start the client 
        try {
            Thread.sleep(1000); // Wait for server to start
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientThread = new Thread(() -> WearableHealthTracker.startClient());
        clientThread.start();
    }
}
