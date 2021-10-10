package com.epam.community.java.tracingdemo.weatherservice;

import brave.Tracer;
import brave.sampler.Sampler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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

    @Bean
    public ExecutorService taskExecutor(BeanFactory beanFactory) {
        return new TraceableExecutorService(beanFactory, Executors.newFixedThreadPool(10));
    }

}
