# Weather Checking Service

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Functionalities](#functionalities)
* [Weather API provider](#weather-api-provider)
* [Instructions on Using Application](#instructions-on-using-application)
* [Functionality Documentation](#functionality-documentation)
* [Future Enhancements](#future-enhancements)

## General info
A simple weather checking API service.
	
## Technologies
* Java 1.8
* Spring Boot 2.4.3
* MySQL 5

## Functionalities

| Case                                                             | User Story                                                                                                                                 |
|------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Search for today's weather of a specific city                    | As an API consumer, one should be able to search for today's weather by inputting the city name.                                           |
| Get historical weather data by ID                                | As an API consumer,  one should be able to look for weather data from past periods                                                         |
| Get historical saved weather data by city AND/OR Generation Date | As an API consumer,  one should be able to look for earlier saved weather data for a specific city with/without mentioning Generation Date |
| Get latest saved weather data by city                            | As an API consumer,  one should be able to look for the latest saved weather data for a specific city                                      |
| Save weather data                                                | As an API consumer, one should be able to save weather data for retrieval                                                                  |
| Save current weather data for a specific city                    | As an API consumer,  one should be able to save directly the current weather data for a specific city for later retrieval                  |
| Update historical weather data                                   | As an API consumer, one should be able to update an existing weather record                                                                |
| Delete historical weather data                                   | As an API consumer,  one should be able to delete an existing weather record                                                               |
| Able to ensure existing functionality is working                 | This design ensures that any changes to the retrieved JSON object from API won't break existing functionality                              |

## Weather API Provider

https://openweathermap.org/current
> Access current weather data for any location on Earth including over 200,000 cities! We collect and process weather data from different sources such as global and local weather models, satellites, radars and vast network of weather stations. Data is available in JSON, XML, or HTML format.


## Instructions on Using Application
1. Run the queries in pre-ddl.sql, dml.sql in this mentioned order in MySQL. Follow DB credentials specified in application.properties.
2. Run the queries in pre-ddl.sql, dml.sql in this mentioned order.
3. Run the project in your IDE.

## Functionality Documentation
1. Use _/getCurrentWeather?city={city}_ **[GET]** to find the latest weather of a _city_. _city_ is a required parameter here.
2. Use _/getSavedWeather?id={id}_ **[GET]** to find a saved weather entry by _id_. _id_ is a required parameter here, specifying the DB id of that record. [GET 
3. Use _/getSavedWeatherList?city={city}_ **[GET]** to find all saved weather entries of a particular city. _city_ is a required parameter here.
4. Use _/getSavedWeatherList?city={city}&created={created}_ **[GET]** to find all saved weather entries of a particular city, on a particular date, _created_. It's just a variation of the previous URL.
5. Use _/getLatestSavedWeather?city={city}_ **[GET]** to find the latest saved weather entry for a specific city. _city_ is required.
6. Use _/saveWeather_ **[POST]** to save an inputted weather entry _(as request body)_ upon validation. The fields' specifications are given below -
```
   {
   "coord": {
        "lat":  Double [non-empty, minValue = -90, maxValue = 90],
        "lang"/"lon": Double [non-empty, minValue = -180, maxValue = 180]
   },
   
   "weatherSummaryList"/"weather": [
   {
         "id": long [if it is provided, then the rest fields should remain empty],
         "summary"/"main": String [non-empty if id not given, minLength = 1, maxLength = 255],
         "description": String [non-empty if id not given, minLength = 1, maxLength = 1024],
         "icon": String [non-empty if id not given, minLength = 7, maxLength = 7, should match this pattern: **[A-Za-z0-9]{3}.png$**, e.g. _01n.png_]
   }
   ...
   ],
   
   "weatherDetail"/"main": {
         "temperature"/"temp": Double [non-empty],
         "feelsLike"/"feels_like": Double [non-empty],
         "minTemperature"/"temp_min": Double [non-empty],
         "maxTemperature"/"temp_max": Double [non-empty],
         "pressure": Integer [non-empty],
         "humidity": Integer [non-empty, minValue = 0, maxValue = 100],
         "seaLevel"/"sea_level": Integer [optional],
         "groundLevel"/"grnd_level": Integer [optional]
   },
   
   "wind": {
         "speed": Double [non-empty],
         "degree"/"deg": Integer [non-empty, minValue = 0, maxValue = 360],
         "gust": Double [optional]
   },
   
   "cloud": {
         "cloudiness"/"all": Integer [non-empty, minValue = 0, maxValue = 100]
   },
   
   "rain": {
        "volumeInLastHr"/"1h": Double [optional],
        "volumeInLast3Hrs"/"3h": Double [optional]
   },
   
   "snow": {
        "volumeInLastHr"/"1h": Double [optional],
        "volumeInLast3Hrs"/"3h": Double [optional]
   },
   
   "name": String [non-empty, minLength = 1, maxLength = 255],
   
   "timezone": Long [non-empty]
   }
```
7. Use _/saveCurrentWeather?city={city}_ **[POST]** to save the current weather of a specific city directly to the system. No request body is needed to save here, provide the city only!
8. Use _/updateWeather?id={id}_ **[PUT]** to update a saved weather entry upon validation. _id_ is required here. Moreover, the request body like the above structure should be given too.
9. Use _/deleteWeather?id={id}_ **[DELETE]** to delete a saved weather entry.

## Future Enhancements
1. Will add **User Registration and Authentication** such that one can register in our system, then use the aforementioned functionalities based on his/her accessibility. That is, if a weather data is not created by him/her, s/he cannot access _(read/save/update)_ it.
2. For the update action, a user has to input all the fields currently. Will develop a handler such that, user only needs to input one or more fields (with id in request parameter surely), and only those fields would be updated in the existing object, the rest would remain the same.