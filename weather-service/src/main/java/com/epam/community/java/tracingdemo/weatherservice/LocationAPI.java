package com.epam.community.java.tracingdemo.weatherservice;

import org.springframework.cloud.sleuth.annotation.NewSpan;

public interface LocationAPI {

    @NewSpan("location-api-get-city-by-ip")
    String getCityByIp(String ip);
}
