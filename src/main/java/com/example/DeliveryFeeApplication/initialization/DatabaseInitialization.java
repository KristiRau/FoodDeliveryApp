package com.example.DeliveryFeeApplication.initialization;

import com.example.DeliveryFeeApplication.model.BaseFee;
import com.example.DeliveryFeeApplication.repository.BaseFeeRepository;
import com.example.DeliveryFeeApplication.repository.WeatherRepository;
import com.example.DeliveryFeeApplication.service.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitialization {

    private final WeatherService weatherService;
    private final WeatherRepository weatherRepository;
    private final BaseFeeRepository baseFeeRepository;

    public DatabaseInitialization(WeatherService weatherService, WeatherRepository weatherRepository,
                                  BaseFeeRepository baseFeeRepository) {
        this.weatherService = weatherService;
        this.weatherRepository = weatherRepository;
        this.baseFeeRepository = baseFeeRepository;
    }
    
    @PostConstruct
    @Transactional
    public void populateDatabaseWhenStartingApplication() {
            weatherService.fetchAndSaveWeatherData();
    }

    @PostConstruct
    @Transactional
    public void initializeBaseFeesIfNeeded() {
        if (baseFeeRepository.count() == 0) {
            saveInitialBaseFeeData();
        }
    }

    private void saveInitialBaseFeeData() {
        if (baseFeeRepository.count() == 0) {
            saveBaseFee("Tallinn-Harku", "Car", 4.0);
            saveBaseFee("Tallinn-Harku", "Scooter", 3.5);
            saveBaseFee("Tallinn-Harku", "Bike", 3.0);

            saveBaseFee("Tartu-Tõravere", "Car", 3.5);
            saveBaseFee("Tartu-Tõravere", "Scooter", 3.0);
            saveBaseFee("Tartu-Tõravere", "Bike", 2.5);

            saveBaseFee("Pärnu", "Car", 3.0);
            saveBaseFee("Pärnu", "Scooter", 2.5);
            saveBaseFee("Pärnu", "Bike", 2.0);
        }
    }

    private void saveBaseFee(String city, String vehicle, double fee) {
            BaseFee newBaseFee = new BaseFee();
            newBaseFee.setCity(city);
            newBaseFee.setVehicle(vehicle);
            newBaseFee.setFee(fee);
            baseFeeRepository.save(newBaseFee);
    }

}
