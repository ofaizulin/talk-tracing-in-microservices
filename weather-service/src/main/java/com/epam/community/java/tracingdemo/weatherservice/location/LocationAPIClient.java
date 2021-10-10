package com.epam.community.java.tracingdemo.weatherservice.location;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LocationAPIClient implements LocationAPI {

    private final RestTemplate restTemplate;

    @Override
    @Cacheable("city-by-ip")
    public String getCityByIp(String ip) {
        val location = restTemplate.getForObject(String.format("http://localhost:8081/location?ip=%s", ip), String.class);
        return JsonPath.read(location, "$.city");
    }
}
