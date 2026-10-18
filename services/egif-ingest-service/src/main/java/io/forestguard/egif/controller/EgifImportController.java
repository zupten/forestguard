package io.forestguard.egif.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.forestguard.egif.service.EgifImportService;
import io.forestguard.egif.service.EgifImportService.ImportResult;

@Profile("dev")
@RestController
@RequestMapping("/api/egif")
public class EgifImportController {

    private final EgifImportService service;

    public EgifImportController(EgifImportService service) {
        this.service = service;
    }

    @PostMapping("/importar")
    public ImportResult importar() throws Exception {
        return service.importar();
    }
}