# Fault Recovery Implementation for Heartbeat Monitoring System

## Team members:

- Ayush Setpal (as8675)
- Parva Shah (ps7384)
- Shridhar Vilas Shinde (ss7536)

## Overview

The goal of this assignment is to implement a **Fault Recovery System** using either **active** or **passive redundancy**. In this implementation, we have used **passive redundancy** with checkpointing to ensure that a backup monitor can take over when the primary monitor fails. The project simulates a wearable device, such as a **Fitbit**, which sends heartbeats to a monitoring system. The focus of the assignment is to design, implement, and test the recovery process, ensuring that failures are properly handled and system continuity is maintained.

The project meets the following requirements:

- **Passive Redundancy**: The backup monitor is activated only when the primary monitor fails.
- **Checkpointing**: State information, including heartbeat sequence and pulse time, is stored to ensure that the backup monitor can resume from the last checkpoint.
- **Heartbeat Monitoring**: The wearable device continuously sends heartbeats to the monitoring system, simulating random failures to test fault detection and recovery.
- **Fault Logging**: Any failures or issues are logged into a dedicated failure log for traceability.

---

## Project Overview

This project is a prototype of a **Fault Recovery System** using **passive redundancy** with checkpointing. The goal of the system is to detect faults in a primary monitoring process and recover via a backup monitoring system, ensuring continued operation. The domain for this project simulates a **Fitbit-like wearable device** that sends real-time heartbeat signals to a monitoring application, which could represent a mobile app. The primary monitoring process receives heartbeats from the wearable device, and in case of failure, a backup monitor takes over to ensure continuous monitoring.

---

## Domain

The domain assumed for this project is a **healthcare wearable device** that monitors heartbeats in real-time, similar to a Fitbit watch. The wearable device communicates with a mobile application via a backend server that monitors heartbeats. If the server detects any failure in communication with the wearable (i.e., missed heartbeats), it triggers a fault recovery process, where a backup monitoring system takes over without losing data, ensuring that the heartbeats are continuously monitored.

---

## Components

### 1. **Wearable Device**

- Simulates a **Fitbit** that sends heartbeats every few seconds to the monitoring system.
- The device can simulate random failures to test the fault detection and recovery mechanisms.

### 2. **Primary Heartbeat Monitor**

- Continuously receives heartbeats from the wearable device.
- In case of failure (no heartbeat received within a timeout period), the primary monitor logs the failure and stops.

### 3. **Backup Heartbeat Monitor**

- Stands by and takes over monitoring when the primary monitor fails.
- It resumes from the last saved checkpoint, continuing from the last known heartbeat sequence.

### 4. **Fault Monitor**

- Logs failure events when the primary or backup monitor detects a failure.

### 5. **Checkpoint Manager**

- Saves and restores the heartbeat sequence and last pulse time to ensure no data is lost during the recovery process.
- A file-based checkpointing system (`checkpoint.txt`) is used for saving and restoring the heartbeat state.

---

## Fault Recovery Process

- The **primary monitor** receives heartbeat signals from the wearable device. If a failure is detected (i.e., no heartbeat is received within the configured timeout), the **backup monitor** takes over.
- The **backup monitor** restores the last saved checkpoint from a file and resumes monitoring from where the primary monitor stopped.
- The **wearable device** reconnects to the backup monitor to continue sending heartbeats.

---

## Key Features

1. **Passive Redundancy**: A backup monitor is in standby mode and takes over only when the primary monitor fails.
2. **Checkpointing**: The system saves the heartbeat sequence number and last pulse time after every successful heartbeat. This checkpoint is restored by the backup monitor upon failure of the primary monitor.
3. **Fault Detection**: Heartbeat timeouts are used to detect when the primary monitor fails. The backup monitor logs any failure events.
4. **Simulated Failures**: The wearable device randomly simulates failures to test the robustness of the fault detection and recovery mechanisms.

---

## How to Run the Project

### Prerequisites

- Java Development Kit (JDK 8 or higher)
- IDE or Command Line Environment

### Steps to Run

1. **Compile the Program**:

   ```bash
   javac -d out -sourcepath src src/Main.java
   ```

2. **Run the Program**:

   ```bash
    java -cp out Main

   ```
