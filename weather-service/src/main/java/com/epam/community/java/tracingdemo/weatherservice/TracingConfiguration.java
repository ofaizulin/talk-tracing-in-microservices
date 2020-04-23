package com.epam.community.java.tracingdemo.weatherservice;

import brave.Tracer;
import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class TracingConfiguration {

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    public OncePerRequestFilter exposeTraceIdFilter(Tracer tracer) {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if (tracer.currentSpan() != null) {
                    response.addHeader("X-TraceId", tracer.currentSpan().context().traceIdString());
                }
                filterChain.doFilter(request, response);
            }
        };
    }

}
