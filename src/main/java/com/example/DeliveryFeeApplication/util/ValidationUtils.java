package com.example.DeliveryFeeApplication.util;

import java.util.List;

public class ValidationUtils {

    public static String isValidCity(String city) {
        List<String> validCities = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
        for (String validCity : validCities) {
            if (validCity.equalsIgnoreCase(city)) {
                return validCity;
            } else if (city != null && validCity.toLowerCase().contains(city.toLowerCase())) {
                return validCity;
            }
        }
        return null;
    }

    public static String isValidVehicleType(String vehicle) {
        List<String> validVehicles = List.of("Car", "Scooter", "Bike");
        for (String validVehicle : validVehicles) {
            if (validVehicle.equalsIgnoreCase(vehicle)) {
                return StringUtils.capitalizeEachWord(vehicle);
            }
        }
        return null;
    }

}
