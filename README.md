# 🚗 Car Management & Fuel Tracking System

Welcome to the **Car Management System**! This project is a robust solution designed to manage car fleets and track fuel consumption efficiently. It features a high-performance **Spring Boot Backend** and an intuitive **Java CLI Client** for easy interaction.

Whether you're a developer looking to explore the code or a user wanting to manage vehicle data, this guide will help you get started quickly.

---

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [1. Building the Project](#1-building-the-project)
  - [2. Running the Backend](#2-running-the-backend)
  - [3. Running the CLI Tool](#3-running-the-cli-tool)
- [Usage Examples](#usage-examples)
- [API Reference](#api-reference)
- [Troubleshooting](#troubleshooting)

---

## 🌟 Project Overview

This system is composed of two main parts:
1.  **Backend Server**: A Spring Boot application serving as the core engine. It manages data in-memory (for speed and simplicity) and exposes RESTful APIs.
2.  **CLI Client**: A command-line interface tool that communicates with the backend, allowing you to create cars, add fuel logs, and view statistics without needing a web browser or Postman.

---

## 🛠 Prerequisites

Before you begin, ensure you have the following installed on your machine:
*   **Java Development Kit (JDK) 17** or higher.
*   **Maven** (optional, as the project includes a Maven wrapper).
*   **Git** (to clone/push the repository).

---

## 🚀 Getting Started

Follow these steps to get the system up and running.

### 1. Building the Project

First, we need to build the executables for both the backend and the CLI.

Open your terminal (PowerShell, Command Prompt, or Bash) and navigate to the `demo` directory:

```bash
cd demo
```

Run the maven build command to compile the code and package the JAR files:

```bash
# On Windows
.\mvnw clean install

# On macOS/Linux
./mvnw clean install
```
*Note: This creates the necessary JAR files in the `target` directories.*

### 2. Running the Backend

The backend server must be running for the system to work.

Still in the `demo` directory, run:

```bash
# On Windows
.\mvnw spring-boot:run

# On macOS/Linux
./mvnw spring-boot:run
```

You should see logs appearing. Wait until you see a message like `Started CarManagementApplication in ... seconds`.
The server is now live at **http://localhost:8080**.

> **Keep this terminal window open!** If you close it, the server stops.

### 3. Running the CLI Tool

Open a **new** terminal window (do not close the backend one) and navigate to the project root `demo` folder again.

To use the CLI, you run the built Java Archive (JAR) file.

```bash
cd demo
```

Run the specific commands as shown in the examples below. The basic syntax is:

```bash
java -jar CLI/target/cli-client-1.0-SNAPSHOT.jar <command> [arguments]
```

---

## 💡 Usage Examples

Here are the most common things you can do.

### 1️⃣ Create a New Car
Register a car in the system.

```bash
java -jar CLI/target/cli-client-1.0-SNAPSHOT.jar create-car --brand Toyota --model Corolla --year 2018
```
*Expected Output: `Car created successfully with ID: 1`*

### 2️⃣ Add Fuel Entry
Log a refueling event for a specific car.

```bash
# Syntax: add-fuel --carId <ID> --liters <AMOUNT> --price <TOTAL_PRICE> --odometer <READING>
java -jar CLI/target/cli-client-1.0-SNAPSHOT.jar add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
```
*Expected Output: `Fuel entry added successfully.`*

### 3️⃣ View Fuel Statistics
See the consumption stats for a car.

```bash
java -jar CLI/target/cli-client-1.0-SNAPSHOT.jar fuel-stats --carId 1
```
*Expected Output: JSON data showing average consumption, total cost, etc.*

---

## 🔌 API Reference

If you prefer using **Postman** or `curl`, here are the available endpoints:

| Action | Method | URL Path | Body Example |
| :--- | :--- | :--- | :--- |
| **Create Car** | `POST` | `/api/cars` | `{ "brand": "BMW", "model": "X5", "year": 2022 }` |
| **List Cars** | `GET` | `/api/cars` | _None_ |
| **Add Fuel** | `POST` | `/api/cars/{id}/fuel` | `{ "liters": 50, "price": 100.0, "odometer": 12000 }` |
| **Get Stats** | `GET` | `/api/cars/{id}/fuel/stats` | _None_ |
| **Get Stats (Servlet)** | `GET` | `/servlet/fuel-stats?carId={id}` | _None_ |

---

## ❓ Troubleshooting

*   **"Connection Refused"**: Ensure the Backend Server is running in a separate terminal.
*   **"Jar not accessed/found"**: Make sure you ran `.\mvnw clean install` in the `demo` folder first. Check if the path `CLI/target/cli-client-1.0-SNAPSHOT.jar` exists.
*   **"Port 8080 already in use"**: Another service is using port 8080. Stop that service or configure a different port in `application.properties`.

---

Happy Coding! 🚙💨
