import Server.HeartbeatMonitor;
import Client.WearableHealthTracker;
import FaultHandler.FaultMonitor;

public class Main {

    public static void main(String[] args) {
        // Start the fault handler 
        Thread faultHandlerThread = new Thread(() -> FaultMonitor.startFaultHandler());
        faultHandlerThread.start();

        // Start the server 
        Thread serverThread = new Thread(() -> HeartbeatMonitor.startServer());
        serverThread.start();

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
