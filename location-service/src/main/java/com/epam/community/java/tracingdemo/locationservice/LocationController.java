package com.epam.community.java.tracingdemo.locationservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationApi locationApi;

    @GetMapping("/location")
    @NewSpan("location-controller-get-location")
    public Location getLocation(@RequestParam @SpanTag("ip") String ip) {
        val city = locationApi.getCityByIp(ip);
        log.info("City for ip {} is {}", ip, city);
        return new Location(ip, city);
    }
}
