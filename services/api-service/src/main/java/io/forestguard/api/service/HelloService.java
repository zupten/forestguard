package io.forestguard.api.service;

import org.springframework.stereotype.Service;

import io.forestguard.api.dto.HelloResponse;

@Service
public class HelloService {

	public HelloResponse obtenerSaludo() {
        return new HelloResponse(
                "ForestGuard",
                "api-service",
                "Hola, ForestGuard está en funcionamiento. 【之Teng】"
        );
    }
}


