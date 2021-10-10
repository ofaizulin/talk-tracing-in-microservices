package com.epam.community.java.tracingdemo.weatherservice.location;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;

public interface LocationAPI {

    @NewSpan("location-api-get-city-by-ip")
    String getCityByIp(@SpanTag("ip") String ip);
}
