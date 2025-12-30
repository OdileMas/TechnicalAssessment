package com.example.carmanagement.demo.config;
import com.example.carmanagement.demo.service.CarService;
import com.example.carmanagement.demo.servlet.FuelStatsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<FuelStatsServlet> fuelStatsServlet(CarService carService) {
        ServletRegistrationBean<FuelStatsServlet> bean = new ServletRegistrationBean<>(
                new FuelStatsServlet(carService), "/servlet/fuel-stats");
        return bean;
    }
}
