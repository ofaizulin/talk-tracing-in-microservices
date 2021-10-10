package com.epam.community.java.tracingdemo.weatherservice;

import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.sleuth.brave.instrument.redis.ClientResourcesBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@SpringBootApplication
public class WeatherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean(destroyMethod = "shutdown")
    DefaultClientResources myLettuceClientResources(ObjectProvider<ClientResourcesBuilderCustomizer> customizer) {
        DefaultClientResources.Builder builder = DefaultClientResources.builder();
        // setting up the builder manually
        customizer.stream().forEach(c -> c.customize(builder));
        return builder.build();
    }
}
