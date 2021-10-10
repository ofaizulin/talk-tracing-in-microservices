package com.epam.community.java.tracingdemo.weatherservice.weather;

import brave.Tracer;
import com.epam.community.java.tracingdemo.weatherservice.location.LocationAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherAPI weatherAPI;
    private final LocationAPI locationAPI;
    private final Tracer tracer;
    private final ExecutorService executorService;

    @GetMapping("/weather")
    @NewSpan("weather-controller-get-weather")
    public ResponseEntity<List<Weather>> getWeather(
            @RequestParam(required = false) @SpanTag(key = "city") List<String> city,
            @RequestParam(required = false) @SpanTag(key = "ip") List<String> ip) {

        city = Optional.ofNullable(city).orElseGet(List::of);
        ip = Optional.ofNullable(ip).orElseGet(List::of);

        List<WeatherResolver> resolvers = new ArrayList<>(city.size() + ip.size());
        resolvers.addAll(city.stream().map(WeatherByCityResolver::new).collect(Collectors.toList()));
        resolvers.addAll(ip.stream().map(WeatherByIpResolver::new).collect(Collectors.toList()));

        // sequential
//        List<Weather> results = resolvers
//                .stream()
//                .map(WeatherResolver::resolve)
//                .collect(Collectors.toList());

        // concurrent
        List<Future<Weather>> callables = resolvers
                .stream()
                .map(r -> executorService.submit(r::resolve))
                .collect(Collectors.toList());
        List<Weather> results = callables.stream().map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }

    private sealed interface WeatherResolver {
        Weather resolve();
    }

    @RequiredArgsConstructor
    private final class WeatherByIpResolver implements WeatherResolver {

        private final String ip;

        @Override
        public Weather resolve() {
            String city = locationAPI.getCityByIp(ip);
            return weatherAPI.getWeatherByCity(city);
        }
    }

    @RequiredArgsConstructor
    private final class WeatherByCityResolver implements WeatherResolver {
        private final String city;

        @Override
        public Weather resolve() {
            return weatherAPI.getWeatherByCity(city);
        }
    }
}
