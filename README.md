# Heartbeat Tactic Implementation

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)

---

## Overview
This project demonstrates the **Heartbeat Architectural Tactic** for fault detection in a critical process. The heartbeat mechanism monitors the health of a critical service, ensuring prompt detection of non-deterministic failures. This prototype includes:
- A **Service** that performs tasks and may randomly fail.
- A **Heartbeat Monitor** that regularly checks the status of the service and takes action when it detects failure.

## Architecture
- **Service (Critical Process)**: Simulates a running service, which randomly fails based on a non-deterministic condition.
- **Heartbeat Monitor**: Periodically sends heartbeats to the service. If the service crashes, the monitor detects the failure and logs the event.
- Both processes run independently in separate threads.

## Prerequisites
- **Java 8** or later
  - Ensure that Java is installed and properly set up in your environment. You can verify installation by running `java -version` in your terminal/command prompt.

## How To Run

- compile: javac -d out -sourcepath src src/Main.java
- run: java -cp out Main