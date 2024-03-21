package com.example.DeliveryFeeApplication.controller;

import com.example.DeliveryFeeApplication.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class DeliveryControllerTest {

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController deliveryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateDeliveryFee_Success() {
        String city = "Tallinn-Harku";
        String vehicle = "Car";
        double expectedFee = 4.0;

        when(deliveryService.calculateDeliveryFee(city, vehicle)).thenReturn(expectedFee);

        ResponseEntity<Double> response = deliveryController.calculateDeliveryFee(city, vehicle);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFee, response.getBody());
        verify(deliveryService, times(1)).calculateDeliveryFee(city, vehicle);
    }

    @Test
    public void testCalculateDeliveryFee_InvalidRequest() {
        String city = "InvalidCity";
        String vehicle = "Car";

        when(deliveryService.calculateDeliveryFee(city, vehicle)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Double> response = deliveryController.calculateDeliveryFee(city, vehicle);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(deliveryService, times(1)).calculateDeliveryFee(city, vehicle);
    }
}