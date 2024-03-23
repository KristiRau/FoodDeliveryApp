package com.example.DeliveryFeeApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "wmo_code")
    private String wmoCode;

    @Column(name = "air_temperature")
    private double airTemperature;

    @Column(name = "wind_speed")
    private double windSpeed;

    @Column(name = "weather_phenomenon")
    private String weatherPhenomenon;

    @Column(name = "observation_timestamp")
    private long observationTimestamp;

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", stationName='" + stationName + '\'' +
                ", wmoCode='" + wmoCode + '\'' +
                ", airTemperature=" + airTemperature +
                ", windSpeed=" + windSpeed +
                ", weatherPhenomenon='" + weatherPhenomenon + '\'' +
                ", observationTimestamp=" + observationTimestamp +
                '}';
    }
}

