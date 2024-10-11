package Utils;

public class HeartbeatConstants {
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8080;

    public static final int FALLBACK_PORT = 8082; // Backup monitor runs on a different port

    public static final int HEARTBEAT_INTERVAL = 2000; // 2 seconds between heartbeats
    public static final int HEARTBEAT_TIMEOUT = 5000;  // 5 seconds timeout for monitoring

    public static final double FAILURE_PROBABILTY = 0.1; // 10% chance of failure per heartbeat

    public static final String ERROR_MANAGER_HOST = "localhost";
    public static final int ERROR_MANAGER_PORT = 8081; // Port for fault handler to listen on

    public static final String TRACE_LOG = "./src/Logger/failure_log.txt"; // Log file location
}
