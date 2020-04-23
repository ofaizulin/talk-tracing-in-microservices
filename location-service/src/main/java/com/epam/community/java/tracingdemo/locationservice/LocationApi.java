package com.epam.community.java.tracingdemo.locationservice;

import org.springframework.cloud.sleuth.annotation.NewSpan;

public interface LocationApi {

    @NewSpan("location-svc-operation-get-location")
    String getCityByIp(String ip);
}
