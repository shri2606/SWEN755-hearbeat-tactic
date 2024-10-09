package Utils;

public class HeartbeatConstants {
    public static final String BACKEND_HOST = "localhost";
    public static final int BACKEND_PORT = 8080;

    public static final int BACKUP_PORT = 8082; // Backup monitor runs on a different port

    public static final int PULSE_INTERVAL = 2000; // 2 seconds between heartbeats
    public static final int PULSE_TIMEOUT = 5000;  // 5 seconds timeout for monitoring

    public static final double RISK_OF_FAILURE = 0.1; // 10% chance of failure per heartbeat

    public static final String ERROR_HANDLER_HOST = "localhost";
    public static final int ERROR_HANDLER_PORT = 8081; // Port for fault handler to listen on

    public static final String DEBUG_LOG = "./src/Logger/failure_log.txt"; // Log file location
}
