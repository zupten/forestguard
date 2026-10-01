package io.forestguard.api.controller;

import java.util.List;

import io.forestguard.api.dto.CrearMuestraRequest;
import io.forestguard.api.dto.MuestraResponse;
import io.forestguard.api.service.MuestraMeteorologicaService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.forestguard.api.service.ImportacionMeteorologicaService;

@RestController
@RequestMapping("/api/muestras")
public class MuestraMeteorologicaController {
	private final ImportacionMeteorologicaService importacionService;
    private final MuestraMeteorologicaService service;

    public MuestraMeteorologicaController(
            MuestraMeteorologicaService service,
            ImportacionMeteorologicaService importacionService) {

        this.service = service;
        this.importacionService = importacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MuestraResponse crear(
    		@Valid @RequestBody CrearMuestraRequest request) {
        return service.guardar(request);
    }
    
    @PostMapping("/importar/valencia")
    @ResponseStatus(HttpStatus.CREATED)
    public MuestraResponse importarValencia() {
        return importacionService.importarValencia();
    }

    @GetMapping
    public List<MuestraResponse> consultarTodas() {
        return service.consultarTodas();
    }
}