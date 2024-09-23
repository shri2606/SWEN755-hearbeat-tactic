# Fault Detection: Heartbeat Tactic for Wearable Health Tracker

## Team members:

- Ayush Setpal (as8675)
- Parva Shah (ps7384)
- Shridhar Vilas Shinde (ss7536)

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)

---

## Overview

This project demonstrates the **Heartbeat Architectural Tactic** for fault detection in a critical process. The heartbeat mechanism monitors the health of a critical service, ensuring prompt detection of non-deterministic failures. This prototype includes:

- A **Service** that performs tasks and may randomly fail.
- A **Heartbeat Monitor** that regularly checks the status of the service and takes action when it detects failure.

## Modules

- **FaultHandler**:

  - Handles failure logging to a file when notified by the receiver of a detected failure.

- **HeartbeatMonitor**:

  - Monitors the heartbeat messages sent from the wearable device (sender) and detects failures if a heartbeat isn’t received within the expected interval.
  - **Receiver**: Receives the heartbeats from the wearable device and tracks the time of the last heartbeat.
  - **Failure Notification**: Sends failure notifications to the **FaultHandler** when a failure is detected.

- **Logger**:

  - Handles logging of detected failures in a log file (failure_log.txt).

- **Sender**:

  - Sends periodic heartbeats (ping messages) from the wearable health tracker to the receiver.

- **Main**:
  - Initializes the processes for the **Sender**, **Receiver**, and **FaultHandler**, running them in separate threads for simulation.

---

## Prerequisites

- **Java 8** or later
  - Ensure that Java is installed and properly set up in your environment. You can verify installation by running `java -version` in your terminal/command prompt.

## How To Run

- compile: javac -d out -sourcepath src src/Main.java
- run: java -cp out Main
