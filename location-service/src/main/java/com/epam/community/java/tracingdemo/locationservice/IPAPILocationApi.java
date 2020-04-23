package com.epam.community.java.tracingdemo.locationservice;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "ipapi")
public class IPAPILocationApi implements LocationApi {

    private final RestTemplate restTemplate;

    @Override
    @Cacheable(key = "'ip2city-' + #ip")
    public String getCityByIp(String ip) {
        return restTemplate.getForObject(String.format("https://ipapi.co/%s/city/", ip), String.class);
    }
}
