package io.forestguard.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.forestguard.api.client.OpenMeteoClient;
import io.forestguard.api.dto.*;

@RestController
@RequestMapping("/api/meteorologia")
public class ConsultaMeteorologicaController {
	private final OpenMeteoClient client;
	
	public ConsultaMeteorologicaController(OpenMeteoClient client) {
		this.client = client;
	}
	
	@GetMapping("/actual")
	public OpenMeteoResponse consultarActual() {
		return client.consultarValencia();
	}
}
