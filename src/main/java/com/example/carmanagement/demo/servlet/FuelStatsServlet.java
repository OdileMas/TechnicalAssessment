package com.example.carmanagement.demo.servlet;

import com.example.carmanagement.demo.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

// Note: We will register this manually as requested, not via @WebServlet
public class FuelStatsServlet extends HttpServlet {
    private final CarService carService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public FuelStatsServlet(CarService carService) {
        this.carService = carService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String carIdParam = req.getParameter("carId");

        if (carIdParam == null || carIdParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing carId parameter");
            return;
        }

        try {
            Long carId = Long.parseLong(carIdParam);
            Map<String, Object> stats = carService.getFuelStats(carId);

            if (stats == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Car not found");
                return;
            }

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(stats));

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid carId format");
        }
    }
}
