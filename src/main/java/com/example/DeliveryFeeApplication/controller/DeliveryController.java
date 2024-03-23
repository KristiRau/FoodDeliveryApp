package com.example.DeliveryFeeApplication.controller;

import com.example.DeliveryFeeApplication.model.Weather;
import com.example.DeliveryFeeApplication.service.DeliveryService;
import com.example.DeliveryFeeApplication.service.WeatherService;
import com.example.DeliveryFeeApplication.util.StringUtils;
import com.example.DeliveryFeeApplication.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/delivery-fee")
    public ResponseEntity<Object> calculateDeliveryFee(@RequestParam String city, @RequestParam String vehicle) {
        String validatedCity = ValidationUtils.isValidCity(city);
        String validatedVehicle = ValidationUtils.isValidVehicleType(vehicle);

        if (validatedCity == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid city: " + city);
        }

        if (validatedVehicle == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid vehicle: " + vehicle);
        }

        if (!deliveryService.isWeatherSuitableForVehicle(validatedCity, validatedVehicle)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usage of selected vehicle type is " +
                    "forbidden due to weather conditions.");
        } else {
            try {
                double fee = deliveryService.calculateDeliveryFee(validatedCity, validatedVehicle);
                return ResponseEntity.ok(fee);
            } catch(IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
    }


}