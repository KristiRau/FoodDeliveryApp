package com.example.DeliveryFeeApplication.service;

import com.example.DeliveryFeeApplication.model.BaseFee;
import com.example.DeliveryFeeApplication.model.Weather;
import com.example.DeliveryFeeApplication.repository.BaseFeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.List;

@Service
public class DeliveryService {

    private static final Logger LOGGER = Logger.getLogger(DeliveryService.class.getName());
    private static final double AIRTEMP_LESS_THAN_MINUS_10 = 1.0;
    private static final double AIRTEMP_BETWEEN_MINUS_10_AND_0 = 0.5;
    private static final double WINDSPEED_BETWEEN_10_AND_20 = 0.5;
    private static final double WEATHER_PHENOMENON_SNOW_OR_SLEET = 1.0;
    private static final double WEATHER_PHENOMENON_RAIN = 0.5;
    private final WeatherService weatherService;
    private final BaseFeeRepository baseFeeRepository;

    public DeliveryService(WeatherService weatherService, BaseFeeRepository baseFeeRepository) {
        this.weatherService = weatherService;
        this.baseFeeRepository = baseFeeRepository;
    }

    public double calculateDeliveryFee(String city, String vehicle) {
        LOGGER.info("Calculating delivery fee for city: " + city + " and vehicle: " + vehicle);

        Weather latestWeather = weatherService.getLatestWeatherDataForCity(city);
        if (latestWeather == null) {
            throw new IllegalArgumentException("No weather data available for " + city);
        }

        double baseFee = calculateBaseFee(city, vehicle);
        LOGGER.info("Base fee calculated for city: " + city + " and vehicle: " + vehicle + ": " + baseFee);

        double extraFee = calculateExtraFeeForWeatherConditions(city, vehicle, latestWeather);
        LOGGER.info("Extra fee calculated for city: " + city + " and vehicle: " + vehicle + ": " + extraFee);

        double totalFee = baseFee + extraFee;
        LOGGER.info("Total delivery fee calculated for city: " + city + " and vehicle: " + vehicle + ": " + totalFee);
        return totalFee;
    }

    public double calculateBaseFee(String city, String vehicle) {
        BaseFee baseFee = baseFeeRepository.findByCityAndVehicle(city, vehicle);
        if (baseFee != null) {
            return baseFee.getFee();
        } else {
            throw new IllegalArgumentException("Base fee data not found for city: " + city + " and vehicle: " + vehicle);
        }
    }

    private double calculateExtraFeeForWeatherConditions(String city, String vehicle, Weather latestWeather) {

        if (latestWeather == null) {
            throw new IllegalArgumentException("No weather data available for " + city);
        }
        double extraFee = 0.0;

        if ("Scooter".equals(vehicle) || "Bike".equals(vehicle)) {
            double airTemperature = latestWeather.getAirTemperature();
            if (airTemperature < -10) {
                extraFee += AIRTEMP_LESS_THAN_MINUS_10;
            } else if (airTemperature >= -10 && airTemperature < 0) {
                extraFee += AIRTEMP_BETWEEN_MINUS_10_AND_0;
            }

            String weatherPhenomenon = latestWeather.getWeatherPhenomenon();
            if ("Snow".equalsIgnoreCase(weatherPhenomenon) || "Sleet".equalsIgnoreCase(weatherPhenomenon)) {
                extraFee += WEATHER_PHENOMENON_SNOW_OR_SLEET;
            } else if ("Rain".equalsIgnoreCase(weatherPhenomenon)) {
                extraFee += WEATHER_PHENOMENON_RAIN;
            }
        }

        if ("Bike".equals(vehicle)) {
            double windSpeed = latestWeather.getWindSpeed();
            if (windSpeed >= 10 && windSpeed <= 20) {
                extraFee += WINDSPEED_BETWEEN_10_AND_20;
            } else if (windSpeed > 20) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden due to high wind speed.");
            }
        }
        return extraFee;
    }

}