package com.cenfotec.backendcodesprint.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class McpClientConfig {

    @Value("${mcp.ai.base-url:http://localhost:8090}")
    private String mcpBaseUrl;

    @Bean(name = "mcpWebClient")
    public WebClient mcpWebClient() {
        return WebClient.builder()
                .baseUrl(mcpBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
