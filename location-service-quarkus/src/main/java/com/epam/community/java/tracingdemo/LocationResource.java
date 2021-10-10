package com.epam.community.java.tracingdemo;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/location")
public class LocationResource {

    @Inject
    @RestClient
    LocationClient locationClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Location cityByIp(@QueryParam("ip") String ip, @Context HttpHeaders headers) {
        val location = locationClient.getCityByIp(ip);
        log.info("Retrieved Location: {}", location);
        return new Location(ip, location);
    }
}