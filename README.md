1. Introduction
   
Overview
The project is for calculating delivery fee based on vehicle type and location and current weather conditions.
Using Java, Spring framework, H2 database and RESTful interface for requesting delivery fee calculations.

Objectives
The primary goals of the project:
Implement a database for storing weather data. 
Scheduled task (CronJob) to import weather data from the Estonian Environment Agency's weather portal.
Functionality to calculate delivery fee based on input parameters(vehicle and city).
Using REST interface to request and retrieve a delivery fee.

Core Modules

The project consists with following core modules:

Database Management: Includes the design and implementation of database tables to store weather data and base fee calculation data.

Weather Data Importer (CronJob): Configurable scheduled task responsible for fetching weather data from
the Estonian Environment Agency's weather portal and storing it in the database.

Delivery Fee Calculator: Implements the logic for calculating delivery fees based on input parameters:
city, vehicle type, and weather conditions.

RESTful API: Exposes endpoints to request delivery fee.

2. Design Overview
   
The classes are organized into packages based on their responsibilities. Each class focuses on a single
responsibility, promoting code reusability and readability.

The application is structured using a layered architecture:   

Controller Layer: Responsible for handling incoming HTTP requests and delegating the processing to the appropriate service classes.

Service Layer: Contains application-specific functionality. Services store operations, interact with repositories.

Repository Layer: Manages the interaction with the database.

Model Layer: Defines the entities and data structures used throughout the application.

Util Layer: Contains utility classes and helper methods used across different parts of the application.

WeatherCronJob Class: For cheduled tasks responsible for importing weather data from external sources.

3. Database
   
Description of the Database Schema
The database schema consists of a table named "weather" to store weather data observations and table named
"base_fee_data" to save data used for calculating base fee. 
Below is the schema breakdown:

Table Name: base_fee_data
Columns:
id (Primary Key): Unique identifier for each record.
city: city for which the base fee is applicable
fee: fee associated with the city and vehicle
vehicle: type of vehicle for which the base fee is applicable
Each row is for storing base fee for specific city and vehicle combo.

Table Name: weather
Columns:
id (Primary Key): Unique identifier for each record.
station_name: Name of the weather station.
wmo_code: WMO code of the weather station.
air_temperature: Air temperature recorded at the weather station.
wind_speed: Wind speed recorded at the weather station.
weather_phenomenon: Description of the weather phenomenon.
observation_timestamp: Timestamp for when the observation was recorded.
extra_fee: stores calculated extra fee value based on weather conditions.
Each row represents a single observation recorded at a specific weather station plus extra fee for given weatherconditions. 

5. CronJob for Weather Data Importing
   
The WeatherCronJob class represents the implementation of a scheduled task (CronJob) responsible
for importing weather data at regular intervals.
The CronJob is configured to execute the fetchAndSaveWeatherDataTask() method according to the
following cron expression: "0 15 * * * *".

Minute: 0 (Every hour)
Second: 0
Hour: 15 (15 minutes after every hour)
Day of Month: Every day
Month: Every month
Day of Week: Every day of the week
This configuration ensures that the task runs once every hour, 15 minutes after the hour.
The fetchAndSaveWeatherDataTask() fetches weather data through WeatherService class from an external source 
(Estonian Environment Agency's weather portal) and saves it to the database using the WeatherRepository.

5. Delivery Fee Calculation
   
Description of Calculating the Delivery Fee
The delivery fee calculation is based on the input parameters (city and vehicle type) and weather conditions.
This includes regional base fee for different cities and additional fees for specific weather conditions.

Explanation of How Input Parameters and Weather Data are Used in the Calculation
The DeliveryService class handles the delivery fee calculation. It retrieves base fee from baseFeeRepository, based on given 
city and vehicle, then gets extra fee from weatherRepository based on given city. 
Finally, it calculates the total delivery fee by summing up the base fee and extra fees.

Example Calculations Provided for Clarity
Given two parameters: city=Tallinn and vehicle=car.
base fee for Tallinn with car = 4€
extra fee for weather conditions with car = 0 €
total delivery fee = 4€

6. REST Interface
   
The REST API provides an endpoint /delivery-fee for calculating the delivery fee based on specified input parameters.
It also validates the parameters city and vehicle. 

Description of Input Parameters and Expected Responses
Input Parameters:
city: Specifies the city for which the delivery fee is to be calculated.
vehicle: Specifies the type of vehicle being used for delivery (e.g., Car, Scooter, Bike).
Expected Responses:
If the calculation is successful, the API returns the calculated delivery fee as a double value.
If the input parameters are invalid or the calculation fails, the API returns a Bad Request (HTTP status code 400).

7. Usage
   
Instructions for Using the Application
Send a GET request to the /delivery-fee endpoint with the required parameters (city and vehicle).
Ensure that the city parameter specifies one of the supported cities (Tallinn, Tartu, Pärnu) and the
vehicle parameter specifies one of the supported vehicle types (Car, Scooter, Bike).
Receive the calculated delivery fee as the response.

8. Additional info
    
I left some code commented out(i.e. what I used before I got the part where i fetch xml data from url to work)
to show what I tried, and logger lines that I used to pinpoint some errors and where in code I 
went wrong if I didn't get the wanted result.
I used postman to send get request, I used Spring Initalizer to create my spring boot project(https://start.spring.io/)
I also got lot of help from https://www.baeldung.com/ and https://spring.io/guides
And I tried using ChatGPT 3.5, it was my first time using it, but it became still handy when trying to solve errors in my code.
