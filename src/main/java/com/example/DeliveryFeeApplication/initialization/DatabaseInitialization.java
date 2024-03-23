package com.example.DeliveryFeeApplication.initialization;

import com.example.DeliveryFeeApplication.model.BaseFee;
import com.example.DeliveryFeeApplication.repository.BaseFeeRepository;
import com.example.DeliveryFeeApplication.repository.WeatherRepository;
import com.example.DeliveryFeeApplication.service.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;

@Component
public class DatabaseInitialization {

    private final WeatherService weatherService;
    private final WeatherRepository weatherRepository;
    private final BaseFeeRepository baseFeeRepository;

    private static final List<String> TARGET_CITIES = Arrays.asList("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
    private static final List<String> VEHICLE_TYPES = Arrays.asList("Car", "Scooter", "Bike");

    public DatabaseInitialization(WeatherService weatherService, WeatherRepository weatherRepository, BaseFeeRepository baseFeeRepository) {
        this.weatherService = weatherService;
        this.weatherRepository = weatherRepository;
        this.baseFeeRepository = baseFeeRepository;
    }
    
    @PostConstruct
    @Transactional
    public void populateDatabaseIfNeeded() {
        if (weatherRepository.count() == 0) {
            weatherService.fetchAndSaveWeatherData();
        }
    }

    @PostConstruct
    @Transactional
    public void initializeBaseFees() {
        if (baseFeeRepository.count() == 0) {
            saveInitialBaseFeeData();
        }
    }

    private void saveInitialBaseFeeData() {
        for (String city : TARGET_CITIES) {
            for (String vehicle : VEHICLE_TYPES) {
                double fee = getInitialFeeForCityAndVehicle(city, vehicle);
                BaseFee existingBaseFee = baseFeeRepository.findByCityAndVehicle(city, vehicle);
                if (existingBaseFee != null) {
                    existingBaseFee.setFee(fee);
                    baseFeeRepository.save(existingBaseFee);
                } else {
                    BaseFee newBaseFee = new BaseFee();
                    newBaseFee.setCity(city);
                    newBaseFee.setVehicle(vehicle);
                    newBaseFee.setFee(fee);
                    baseFeeRepository.save(newBaseFee);
                }
            }
        }
    }

    private double getInitialFeeForCityAndVehicle(String city, String vehicle) {
        switch (city) {
            case "Tallinn-Harku":
                switch (vehicle) {
                    case "Car":
                        return 4.0;
                    case "Scooter":
                        return 3.5;
                    case "Bike":
                        return 3.0;
                }
            case "Tartu-Tõravere":
                switch (vehicle) {
                    case "Car":
                        return 3.5;
                    case "Scooter":
                        return 3.0;
                    case "Bike":
                        return 2.5;
                }
            case "Pärnu":
                switch (vehicle) {
                    case "Car":
                        return 3.0;
                    case "Scooter":
                        return 2.5;
                    case "Bike":
                        return 2.0;
                }
            default:
                return 0.0;
        }
    }

}
