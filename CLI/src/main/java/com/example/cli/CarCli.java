package com.example.cli;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CarCli {

    private static final String BASE_URL = "http://localhost:8080/api/cars";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        try {
            switch (command) {
                case "create-car":
                    createCar(args);
                    break;
                case "add-fuel":
                    addFuel(args);
                    break;
                case "fuel-stats":
                    fuelStats(args);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    printUsage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  create-car --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel --carId <id> --liters <liters> --price <price> --odometer <odo>");
        System.out.println("  fuel-stats --carId <id>");
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i < args.length; i += 2) {
            if (i + 1 < args.length && args[i].startsWith("--")) {
                map.put(args[i].substring(2), args[i + 1]);
            }
        }
        return map;
    }

    private static void createCar(String[] args) throws IOException, InterruptedException {
        Map<String, String> params = parseArgs(args);
        if (!params.containsKey("brand") || !params.containsKey("model") || !params.containsKey("year")) {
            System.out.println("Missing arguments for create-car");
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("brand", params.get("brand"));
        body.put("model", params.get("model"));
        body.put("year", Integer.parseInt(params.get("year")));

        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.body());
    }

    private static void addFuel(String[] args) throws IOException, InterruptedException {
        Map<String, String> params = parseArgs(args);
        if (!params.containsKey("carId") || !params.containsKey("liters") ||
                !params.containsKey("price") || !params.containsKey("odometer")) {
            System.out.println("Missing arguments for add-fuel");
            return;
        }

        String carId = params.get("carId");
        Map<String, Object> body = new HashMap<>();
        body.put("liters", Double.parseDouble(params.get("liters")));
        body.put("price", Double.parseDouble(params.get("price")));
        body.put("odometer", Double.parseDouble(params.get("odometer")));

        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + carId + "/fuel"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println("Fuel added successfully.");
        } else {
            System.out.println("Error adding fuel: " + response.statusCode());
        }
    }

    private static void fuelStats(String[] args) throws IOException, InterruptedException {
        Map<String, String> params = parseArgs(args);
        if (!params.containsKey("carId")) {
            System.out.println("Missing carId for fuel-stats");
            return;
        }

        String carId = params.get("carId");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + carId + "/fuel/stats"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Map<?, ?> stats = objectMapper.readValue(response.body(), Map.class);
            System.out.println("Total fuel: " + stats.get("totalFuel") + " L");
            System.out.println("Total cost: " + stats.get("totalCost"));
            System.out.println("Average consumption: " + stats.get("averageConsumption") + " L/100km");
        } else {
            System.out.println("Error getting stats: " + response.statusCode());
        }
    }
}
