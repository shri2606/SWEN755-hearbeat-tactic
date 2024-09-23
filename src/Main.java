import Client.WearableHealthTracker;
import FaultHandler.FaultMonitor;
import Server.HeartbeatMonitor;

public class Main {

    public static void main(String[] args) {
        // Start the fault handler 
        Thread faultDetectionThread = new Thread(() -> FaultMonitor.startFaultHandler());
        faultDetectionThread.start();

        // Start the server 
        Thread serverThread = new Thread(() -> HeartbeatMonitor.initializeServer());
        serverThread.start();

        // Start the client 
        try {
            Thread.sleep(1000); // Wait for server to start
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientThread = new Thread(() -> WearableHealthTracker.connectClient());
        clientThread.start();
    }
}
