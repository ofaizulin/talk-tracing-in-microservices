package com.epam.community.java.tracingdemo.weatherservice;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LocationAPIClient implements LocationAPI {

    private final RestTemplate restTemplate;

    @Override
    public String getCityByIp(String ip) {
        val location = restTemplate.getForObject(String.format("http://localhost:8081/location?ip=%s", ip), String.class);
        return JsonPath.read(location, "$.city");
    }
}
