package com.example.DeliveryFeeApplication.service;

import com.example.DeliveryFeeApplication.model.BaseFee;
import com.example.DeliveryFeeApplication.model.Weather;
import com.example.DeliveryFeeApplication.repository.BaseFeeRepository;
import com.example.DeliveryFeeApplication.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class DeliveryService {

    private static final Logger LOGGER = Logger.getLogger(DeliveryService.class.getName());
    private final WeatherService weatherService;
    private final BaseFeeRepository baseFeeRepository;
    private final WeatherRepository weatherRepository;

    public DeliveryService(WeatherService weatherService, BaseFeeRepository baseFeeRepository, WeatherRepository weatherRepository) {
        this.weatherService = weatherService;
        this.baseFeeRepository = baseFeeRepository;
        this.weatherRepository = weatherRepository;
    }

    public double calculateDeliveryFee(String city, String vehicle) {
        LOGGER.info("Calculating delivery fee for city: " + city + " and vehicle: " + vehicle);

        Weather latestWeather = weatherService.getLatestWeatherDataForCity(city);
        LOGGER.info("latestWeather" + latestWeather);
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
            extraFee = weatherRepository.findLatestExtraFeeByCity(city);
        }
        return extraFee;
    }

    public boolean isWeatherSuitableForVehicle(String city, String vehicle) {
        Weather latestWeather = weatherService.getLatestWeatherDataForCity(city);
        if (latestWeather != null && ("Bike".equals(vehicle) || "Scooter".equals(vehicle))) {
            return !(latestWeather.getWeatherPhenomenon().contains("glaze") ||
                    latestWeather.getWeatherPhenomenon().contains("hail") ||
                    latestWeather.getWeatherPhenomenon().contains("thunder") ||
                    latestWeather.getWindSpeed() > 20);
        }
        return false;
    }



   /* private double calculateExtraFeeForWeatherConditions(String city, String vehicle, Weather latestWeather) {

        if (latestWeather == null || vehicle.equals("Car")) {
            throw new IllegalArgumentException("No weather data available for " + city + " or no extra fee " +
                    "available for selected vehicle " + vehicle);
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
    }*/

}