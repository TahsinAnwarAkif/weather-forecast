package com.oddle.app.weather.util;

/**
 * @author akif
 * @since 1/13/22
 */
public class Constants {

    public static final String CURRENT_CWD_URL = "/getCurrentWeather";
    public static final String SAVED_CWD_URL = "/getSavedWeather";
    public static final String LATEST_SAVED_CWD_URL = "/getLatestSavedWeather";
    public static final String SAVED_CWD_LIST_URL = "/getSavedWeatherList";
    public static final String CURRENT_CWD_SAVE_URL = "/saveCurrentWeather";
    public static final String CWD_SAVE_URL = "/saveWeather";
    public static final String CWD_UPDATE_URL = "/updateWeather";
    public static final String CWD_UPDATE_FIELDS_URL = "/updateWeatherFields";
    public static final String CWD_DELETE_URL = "/deleteWeather";

    public static final String CWD_API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    public static final String CWD_API_KEY = "89e287aeda48ec65e04eafb741a2ad46";

    public static final String DATE_PATTERN = "MM-dd-yy";
    public static final String ICON_PATTERN = "[A-Za-z0-9]{3}.png$";

    public static final Integer DEFAULT_PRIMITIVE_VALUE = 0;
}
