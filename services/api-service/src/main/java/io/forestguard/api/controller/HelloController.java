package io.forestguard.api.controller;

import io.forestguard.api.dto.HelloResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public HelloResponse hello() {
        return new HelloResponse(
                "ForestGuard",
                "api-service",
                "Hola, ForestGuard está en funcionamiento. 【之Teng】"
        );
    }
}