package com.epam.community.java.tracingdemo.weatherservice;

import brave.Tracer;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OpenWeatherAPIImpl implements WeatherAPI {

    private final RestTemplate restTemplate;
    private final Tracer tracer;

    @Override
    @NewSpan("open-weather-api-get-weather-by-city")
    public Weather getWeatherByCity(String city) {
        val url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=fded2a18f1109e25b04c0d32315436e4", city);
        val response = restTemplate.getForObject(url, String.class);

        final String weather = JsonPath.read(response, "$.weather[0].main");
        final String weatherDesc = JsonPath.read(response, "$.weather[0].description");
        final double temp = JsonPath.read(response, "$.main.temp");
        final double tempFeelsLike = JsonPath.read(response, "$.main.feels_like");
        final int humidity = JsonPath.read(response, "$.main.humidity");
        final String country = JsonPath.read(response, "$.sys.country");

        return new Weather(city, country, temp, tempFeelsLike, humidity, weather, weatherDesc);
    }
}
