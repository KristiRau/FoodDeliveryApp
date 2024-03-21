package com.example.DeliveryFeeApplication;

import com.example.DeliveryFeeApplication.repository.WeatherRepository;
import com.example.DeliveryFeeApplication.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;

@Component
public class WeatherCronJob {

    private final WeatherService weatherService;
    private final WeatherRepository weatherRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    public WeatherCronJob(WeatherService weatherService, WeatherRepository weatherRepository) {
        this.weatherService = weatherService;
        this.weatherRepository = weatherRepository;
    }

    @Scheduled(cron = "0 15 * * * *")
    public void fetchAndSaveWeatherDataTask() {
        weatherService.fetchAndSaveWeatherData();
    }

    @PostConstruct
    @Transactional
    public void populateDatabaseIfNeeded() {
        if (weatherRepository.count() == 0) {
            weatherService.fetchAndSaveWeatherData();
        }
    }
}

