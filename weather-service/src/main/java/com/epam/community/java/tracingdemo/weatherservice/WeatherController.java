package com.epam.community.java.tracingdemo.weatherservice;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherAPI weatherAPI;
    private final LocationAPI locationAPI;
    private final Tracer tracer;

    @GetMapping("/weather")
    @NewSpan("weather-controller-get-weather")
    public ResponseEntity<Weather> getWeatherByCity(
            @RequestParam(required = false) @SpanTag(key = "city") String city,
            @RequestParam(required = false) @SpanTag(key = "ip") String ip) {

        tracer.newChild(tracer.currentSpan().context());

        if (StringUtils.isEmpty(city) && StringUtils.isEmpty(ip)) {
            return ResponseEntity.badRequest().build();
        }

        if (!StringUtils.isEmpty(ip)) {
            city = locationAPI.getCityByIp(ip);
        }

        return ResponseEntity.ok(weatherAPI.getWeatherByCity(city));
    }
}
