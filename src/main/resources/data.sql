CREATE TABLE IF NOT EXISTS weather (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    station_name VARCHAR(255),
    wmo_code VARCHAR(255),
    air_temperature DOUBLE,
    wind_speed DOUBLE,
    weather_phenomenon VARCHAR(255),
    extra_fee DOUBLE,
    observation_timestamp BIGINT
);

INSERT INTO weather (station_name, wmo_code, air_temperature, wind_speed, weather_phenomenon, observation_timestamp)
VALUES
    ('Tallinn-Harku', '26038', 0.0, 4.4, 'Clear', 1731684488),
    ('Tartu-Tõravere', 'XXXXX', -1.4, 6.5, 'Cloudy', 1731684488),
    ('Pärnu', 'XXXXX', 0.0, 0.0, 'Sunny', 1731684488);