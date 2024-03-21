package com.example.DeliveryFeeApplication.service;

import com.example.DeliveryFeeApplication.model.Weather;
import com.example.DeliveryFeeApplication.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherService {

    private static final String WEATHER_DATA_URL =
            "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final List<String> TARGET_STATIONS =
            Arrays.asList("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
    private final WeatherRepository weatherRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Weather getLatestWeatherDataForCity(String city) {
        return weatherRepository.findFirstByStationNameOrderByIdDesc(city);
    }

   public void fetchAndSaveWeatherData() {
        try {
            URI uri = new URI(WEATHER_DATA_URL);
            URL url = uri.toURL();
            InputStream inputStream = url.openStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            long observationTimestamp = Long.parseLong(doc.getDocumentElement().getAttribute("timestamp"));
            NodeList stationList = doc.getElementsByTagName("station");

            for (int i = 0; i < stationList.getLength(); i++) {
                Element stationElement = (Element) stationList.item(i);
                String stationName = stationElement.getElementsByTagName("name").item(0).getTextContent();
                if (TARGET_STATIONS.contains(stationName)) {
                    Weather weather = new Weather();
                    weather.setStationName(stationName);
                    weather.setWmoCode(stationElement.getElementsByTagName("wmocode").item(0).getTextContent());
                    weather.setAirTemperature(Double.parseDouble(stationElement.getElementsByTagName("airtemperature").item(0).getTextContent()));
                    weather.setWindSpeed(Double.parseDouble(stationElement.getElementsByTagName("windspeed").item(0).getTextContent()));
                    weather.setWeatherPhenomenon(stationElement.getElementsByTagName("phenomenon").item(0).getTextContent());
                    weather.setObservationTimestamp(observationTimestamp);
                    weatherRepository.save(weather);
                }
            }

            inputStream.close();
        } catch (Exception e) {
            LOGGER.error("An error occurred while fetching and saving weather data: {}", e.getMessage(), e);
        }
    }

    /* Used this before I got fetching data from url to work.
    @PostConstruct
    public void populateDatabaseIfNeeded() {
        LOGGER.info("Fetching and saving weather data on application startup.");
        List<Weather> initialWeatherData = weatherRepository.findAll();
        if (initialWeatherData.isEmpty()) {
            LOGGER.info("No data in database, using data.sql");

            try {
                Resource resource = new ClassPathResource("data.sql");
                ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
                LOGGER.info("Weather data saved to the database.");
            } catch (SQLException e) {
                LOGGER.error("Failed to populate database with initial data: {}", e.getMessage(), e);
            }
        } else {
            LOGGER.info("Weather data already exists in the database.");
        }
    }*/
}