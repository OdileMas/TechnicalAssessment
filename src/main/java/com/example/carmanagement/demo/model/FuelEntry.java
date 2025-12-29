package com.example.carmanagement.demo.model;
import java.time.LocalDateTime;

public class FuelEntry {
    private double liters;
    private double price;
    private double odometer;
    private LocalDateTime timestamp;

    public FuelEntry() {
    }

    public FuelEntry(double liters, double price, double odometer) {
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
        this.timestamp = LocalDateTime.now();
    }

    public double getLiters() {
        return liters;
    }

    public void setLiters(double liters) {
        this.liters = liters;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
