package com.epam.community.java.tracingdemo.weatherservice.weather;

public interface WeatherAPI {

    Weather getWeatherByCity(String city);
}
