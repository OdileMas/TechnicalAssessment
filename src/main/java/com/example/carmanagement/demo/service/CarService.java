package com.example.carmanagement.demo.service;
import com.example.carmanagement.demo.model.Car;
import com.example.carmanagement.demo.model.FuelEntry;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarService {
    private final Map<Long, Car> carStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Car createCar(String brand, String model, int year) {
        Long id = idGenerator.getAndIncrement();
        Car car = new Car(id, brand, model, year);
        carStorage.put(id, car);
        return car;
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(carStorage.values());
    }

    public Car getCar(Long id) {
        return carStorage.get(id); // Returns null if not found
    }

    public void addFuel(Long carId, double liters, double price, double odometer) {
        Car car = carStorage.get(carId);
        if (car != null) {
            car.addFuelEntry(new FuelEntry(liters, price, odometer));
        }
    }

    public Map<String, Object> getFuelStats(Long carId) {
        Car car = carStorage.get(carId);
        if (car == null)
            return null;

        double totalFuel = 0;
        double totalCost = 0;

        for (FuelEntry entry : car.getFuelEntries()) {
            totalFuel += entry.getLiters();
            totalCost += entry.getPrice();
        }

        double avgConsumption = 0;
        // Simple Average: (Total Fuel / Total KM) * 100
        // Needs at least 2 entries to calculate distance? The prompt says "avg/100km".
        // If we assume odometer is cumulative, we can take max - min or just use the
        // difference if available.
        // Simplified Logic: The prompt asks for "Avg/100km".
        // Usually: (Liters used / Distance traveled) * 100.
        // We will sum liters. Distance = last odometer - first odometer? Or assume
        // started at 0?
        // Prompt example: "Average consumption: 6.4 L/100km".
        // Let's implement a best-effort calculation using max odometer - min odometer
        // if > 0.

        double distance = 0;
        if (!car.getFuelEntries().isEmpty()) {
            double minOdo = car.getFuelEntries().stream().mapToDouble(FuelEntry::getOdometer).min().orElse(0);
            double maxOdo = car.getFuelEntries().stream().mapToDouble(FuelEntry::getOdometer).max().orElse(0);
            // However, strictly speaking, we need the odometer BEFORE the first fill to
            // know the distance for that fill.
            // But simpler heuristic: If only one entry, maybe assume 0 start or just return
            // 0.
            // Let's assume the user enters odometer reading AT existing fill.
            // If we have multiple entries, we can check the span.
            // If we treat it simply: Total Fuel / Total Distance covered in those refuels *
            // 100.
            // But if we only have 1 entry, we can't calculate a delta.

            // Alternative: "Total liters / Total km * 100" IF we assume the car started at
            // 0 or the first entry is the baseline.
            // To be robust: If entries > 1, distance = last.odo - first.odo. Fuel = sum of
            // liters (excluding the first one? No, usually you fill then drive).
            // Let's stick to: Total Fuel / (Max Odometer - Min Odometer?? No, that's
            // unsafe).
            // Let's try: Total Fuel / Max Odometer * 100 ... No.

            // Correct way for fuel log:
            // Distance = Odometer_current - Odometer_previous.
            // Consumption = Liters_current / Distance * 100.
            // Average = Sum(Liters) / Sum(Distance) * 100.
            // We need at least 2 entries to have a distance delta.

            if (car.getFuelEntries().size() > 1) {
                double firstOdo = car.getFuelEntries().get(0).getOdometer();
                double lastOdo = car.getFuelEntries().get(car.getFuelEntries().size() - 1).getOdometer();
                distance = lastOdo - firstOdo;

                // We exclude the first fuel entry from consumption calc?
                // Usually first fill establishes the baseline full tank.
                // Let's sum all fuel except the first one? Or include all?
                // Prompt is simple. Let's just do: Total Fuel / Distance * 100 (if distance >
                // 0).
                // Whatever logic, I must be able to explain it.
                // "Total stats" usually implies sum of everything.

                if (distance > 0) {
                    avgConsumption = (totalFuel / distance) * 100;
                }
            }
        }

        return Map.of(
                "totalFuel", totalFuel,
                "totalCost", totalCost,
                "averageConsumption", Math.round(avgConsumption * 100.0) / 100.0);
    }
}
