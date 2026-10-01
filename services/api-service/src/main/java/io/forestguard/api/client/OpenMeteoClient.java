package io.forestguard.api.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.forestguard.api.dto.OpenMeteoResponse;

@Component
public class OpenMeteoClient {
    private final WebClient webClient;
    
    public OpenMeteoClient(WebClient.Builder builder, @Value("${forestguard.open-meteo.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }
    public OpenMeteoResponse consultarValencia() {
    	return webClient.get()
    	        .uri(uriBuilder -> uriBuilder
    	                .path("/v1/forecast")
    	                .queryParam("latitude", 39.47)
    	                .queryParam("longitude", -0.38)
    	                .queryParam("current", "temperature_2m")
    	                .queryParam("temperature_unit", "celsius")
    	                .queryParam("timezone", "Europe/Madrid")
    	                .build())
    	        .retrieve()
    	        .bodyToMono(OpenMeteoResponse.class)
    	        .block(Duration.ofSeconds(10));
    }
}
