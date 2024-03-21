package com.example.DeliveryFeeApplication.service;

import com.example.DeliveryFeeApplication.model.Weather;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;
import java.util.List;

@Service
public class DeliveryService {

    private static final Logger LOGGER = Logger.getLogger(DeliveryService.class.getName());
    private static final double TALLINN_CAR = 4.0;
    private static final double TALLINN_SCOOTER = 3.5;
    private static final double TALLINN_BIKE = 3.0;
    private static final double TARTU_CAR = 3.5;
    private static final double TARTU_SCOOTER = 3.0;
    private static final double TARTU_BIKE = 2.5;
    private static final double PÄRNU_CAR = 3.0;
    private static final double PÄRNU_SCOOTER = 2.5;
    private static final double PÄRNU_BIKE = 2.0;
    private static final double AIRTEMP_LESS_THAN_MINUS_10 = 1.0;
    private static final double AIRTEMP_BETWEEN_MINUS_10_AND_0 = 0.5;
    private static final double WINDSPEED_BETWEEN_10_AND_20 = 0.5;
    private static final double WEATHER_PHENOMENON_SNOW_OR_SLEET = 1.0;
    private static final double WEATHER_PHENOMENON_RAIN = 0.5;
    private final WeatherService weatherService;


    public DeliveryService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public double calculateDeliveryFee(String city, String vehicle) {
        LOGGER.info("Calculating delivery fee for city: " + city + " and vehicle: " + vehicle);
        String validatedCity = isValidCity(city);
        Weather latestWeather = weatherService.getLatestWeatherDataForCity(validatedCity);
        Weather weather = new Weather();
        weather = latestWeather;
        LOGGER.info("Latest weather data for city: " + validatedCity + ": " + weather);

        if (latestWeather == null) {
            throw new IllegalArgumentException("No weather data available for " + validatedCity);
        }


        double baseFee = calculateBaseFee(validatedCity, vehicle);
        LOGGER.info("Base fee calculated for city: " + validatedCity + " and vehicle: " + vehicle + ": " + baseFee);

        double extraFee = calculateExtraFeeForWeatherConditions(validatedCity, vehicle, latestWeather);
        LOGGER.info("Extra fee calculated for city: " + validatedCity + " and vehicle: " + vehicle + ": " + extraFee);

        double totalFee = baseFee + extraFee;
        LOGGER.info("Total delivery fee calculated for city: " + validatedCity + " and vehicle: " + vehicle + ": " + totalFee);
        return totalFee;
    }


    public double calculateBaseFee(String city, String vehicle) {
        String validCity = isValidCity(city);
        if (validCity == null) {
            throw new IllegalArgumentException("Invalid city: " + null);
        }
        if (!isValidVehicleType(vehicle)) {
            throw new IllegalArgumentException("Invalid vehicle type: " + vehicle);
        }

        return switch (validCity) {
            case "Tallinn-Harku" -> switch (vehicle) {
                case "Car" -> TALLINN_CAR;
                case "Scooter" -> TALLINN_SCOOTER;
                case "Bike" -> TALLINN_BIKE;
                default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicle);
            };
            case "Tartu-Tõravere" -> switch (vehicle) {
                case "Car" -> TARTU_CAR;
                case "Scooter" -> TARTU_SCOOTER;
                case "Bike" -> TARTU_BIKE;
                default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicle);
            };
            case "Pärnu" -> switch (vehicle) {
                case "Car" -> PÄRNU_CAR;
                case "Scooter" -> PÄRNU_SCOOTER;
                case "Bike" -> PÄRNU_BIKE;
                default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicle);
            };
            default -> throw new IllegalArgumentException("Invalid city: " + validCity);
        };
    }

    private double calculateExtraFeeForWeatherConditions(String city, String vehicle, Weather latestWeather) {
        String validCity = isValidCity(city);

        if (latestWeather == null) {
            throw new IllegalArgumentException("No weather data available for " + validCity);
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


    private String isValidCity(String city) {
        List<String> validCities = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
        for (String validCity : validCities) {
            if (validCity.equalsIgnoreCase(city)) {
                return validCity;
            } else if (validCity.contains(city)) {
                return validCity;
            }
        }
        return null;
    }

    private boolean isValidVehicleType(String vehicle) {
        List<String> validVehicles = List.of("Car", "Scooter", "Bike");
        return vehicle != null && validVehicles.contains(vehicle);
    }

}