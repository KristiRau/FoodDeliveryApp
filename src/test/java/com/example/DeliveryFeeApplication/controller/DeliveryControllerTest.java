package com.example.DeliveryFeeApplication.controller;

import com.example.DeliveryFeeApplication.service.DeliveryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(DeliveryController.class)
public class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;

    @Test
    public void testCalculateDeliveryFee_Success() throws Exception {
        String city = "Tallinn";
        String vehicle = "Car";
        double expectedFee = 4.0;
        when(deliveryService.calculateDeliveryFee(city, vehicle)).thenReturn(expectedFee);
        mockMvc.perform(get("/delivery-fee")
                        .param("city", city)
                        .param("vehicle", vehicle))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedFee));
    }

    @Test
    public void testCalculateDeliveryFee_InvalidRequest() throws Exception {
        String city = "InvalidCity";
        String vehicle = "Car";

        when(deliveryService.calculateDeliveryFee(city, vehicle)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/delivery-fee")
                        .param("city", city)
                        .param("vehicle", vehicle))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }
}