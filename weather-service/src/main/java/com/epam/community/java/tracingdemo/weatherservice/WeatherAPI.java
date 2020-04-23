package com.epam.community.java.tracingdemo.weatherservice;

import org.springframework.cloud.sleuth.annotation.NewSpan;

public interface WeatherAPI {

    @NewSpan("weather-api-get-weather-by-city")
    Weather getWeatherByCity(String city);
}
