package com.cenfotec.backendcodesprint.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RecommendationWebClientConfig {

    @Value("${recommendation.ai.base-url:http://localhost:8091}")
    private String recommendationBaseUrl;

    @Bean(name = "recommendationWebClient")
    public WebClient recommendationWebClient() {
        return WebClient.builder()
                .baseUrl(recommendationBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}