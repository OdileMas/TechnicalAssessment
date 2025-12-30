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
       
        double distance = 0;
        if (!car.getFuelEntries().isEmpty()) {
            double minOdo = car.getFuelEntries().stream().mapToDouble(FuelEntry::getOdometer).min().orElse(0);
            double maxOdo = car.getFuelEntries().stream().mapToDouble(FuelEntry::getOdometer).max().orElse(0);
            
            if (car.getFuelEntries().size() > 1) {
                double firstOdo = car.getFuelEntries().get(0).getOdometer();
                double lastOdo = car.getFuelEntries().get(car.getFuelEntries().size() - 1).getOdometer();
                distance = lastOdo - firstOdo;

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
