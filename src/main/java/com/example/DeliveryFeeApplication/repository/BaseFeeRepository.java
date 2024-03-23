package com.example.DeliveryFeeApplication.repository;

import com.example.DeliveryFeeApplication.model.BaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseFeeRepository extends JpaRepository<BaseFee, Long> {
    BaseFee findByCityAndVehicle(String city, String vehicle);

}