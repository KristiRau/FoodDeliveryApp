package com.example.DeliveryFeeApplication.controller;

import com.example.DeliveryFeeApplication.service.DeliveryService;
import com.example.DeliveryFeeApplication.util.StringUtils;
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
    public ResponseEntity<Double> calculateDeliveryFee(@RequestParam String city, @RequestParam String vehicle) {
        try {
            city = StringUtils.capitalizeEachWord(city);
            vehicle = StringUtils.capitalizeEachWord(vehicle);

            double fee = deliveryService.calculateDeliveryFee(city, vehicle);
            return ResponseEntity.ok(fee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}