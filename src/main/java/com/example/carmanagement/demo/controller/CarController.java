package com.example.carmanagement.demo.controller;
import com.example.carmanagement.demo.model.Car;
import com.example.carmanagement.demo.model.FuelEntry;
import com.example.carmanagement.demo.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car created = carService.createCar(car.getBrand(), car.getModel(), car.getYear());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Car>> listCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @PostMapping("/{id}/fuel")
    public ResponseEntity<?> addFuel(@PathVariable Long id, @RequestBody FuelEntry fuelEntry) {
        Car car = carService.getCar(id);
        if (car == null) {
            return ResponseEntity.notFound().build();
        }
        carService.addFuel(id, fuelEntry.getLiters(), fuelEntry.getPrice(), fuelEntry.getOdometer());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/fuel/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        Map<String, Object> stats = carService.getFuelStats(id);
        if (stats == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }
}
