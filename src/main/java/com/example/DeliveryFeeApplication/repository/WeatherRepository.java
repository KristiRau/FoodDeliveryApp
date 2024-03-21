package com.example.DeliveryFeeApplication.repository;

import com.example.DeliveryFeeApplication.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Weather findFirstByStationNameOrderByIdDesc(@Param("stationName") String stationName);
}
