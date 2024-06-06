package com.gpbitfactory.middle.config;

import com.gpbitfactory.middle.service.MiddleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class MiddleConfig {
    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${backendService.url}") String backUri) {
        return builder
                .rootUri(backUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setConnectTimeout(Duration.ofSeconds(9))
                .setReadTimeout(Duration.ofSeconds(9))
                .build();
    }

    @Bean
    public MiddleService middleService() {
        return new MiddleService(restTemplate(restTemplateBuilder(), null));
    }
}
