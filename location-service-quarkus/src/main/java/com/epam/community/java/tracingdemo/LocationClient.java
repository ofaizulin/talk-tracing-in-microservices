package com.epam.community.java.tracingdemo;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RegisterRestClient(configKey = "location-api")
public interface LocationClient {

    @GET
    @Path("/{ip}/city")
    @Produces("*/*")
    String getCityByIp(@PathParam String ip);
}
