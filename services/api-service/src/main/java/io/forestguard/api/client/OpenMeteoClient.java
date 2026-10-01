package io.forestguard.api.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.forestguard.api.dto.OpenMeteoResponse;

import java.util.concurrent.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

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
    	        .retrieve().bodyToMono(OpenMeteoResponse.class)
    	        .timeout(Duration.ofSeconds(10))
    	        .onErrorMap(
    	                TimeoutException.class,
    	                ex -> new ResponseStatusException(
    	                        HttpStatus.GATEWAY_TIMEOUT,
    	                        "El servicio meteorologico no responde a tiempo",
    	                        ex))
    	        .onErrorMap(
    	                WebClientException.class,
    	                ex -> new ResponseStatusException(
    	                        HttpStatus.BAD_GATEWAY,
    	                        "No se pudo consultar el servicio meteorologico",
    	                        ex))
    	        .block();
    }
}
