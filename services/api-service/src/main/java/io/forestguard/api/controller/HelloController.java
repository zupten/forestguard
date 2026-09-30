package io.forestguard.api.controller;

import io.forestguard.api.dto.HelloResponse;
import io.forestguard.api.service.HelloService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloService helloService;
    
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }
    
    @GetMapping("/api/hello")
    public HelloResponse hello() {
        return helloService.obtenerSaludo();
    }

}