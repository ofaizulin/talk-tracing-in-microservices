package com.epam.community.java.tracingdemo.weatherservice.weather;

import lombok.Value;

@Value
public class Weather {
    String city;
    String country;
    double temperature;
    double feelsLike;
    double humidity;
    String weather;
    String description;
}
