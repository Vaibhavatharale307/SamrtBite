


package com.smartbite.mainservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient walletRestClient(
            @Value("${wallet.service.base-url}") String walletBaseUrl,
            @Value("${internal.api.key}") String internalApiKey) {

        return RestClient.builder()
                .baseUrl(walletBaseUrl)
                .defaultHeader("X-Internal-Api-Key", internalApiKey)
                .build();
    }
}
