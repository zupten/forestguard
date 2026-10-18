package io.forestguard.egif.controller;

import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.forestguard.egif.dto.IncendioEgif;
import io.forestguard.egif.reader.EgifXmlReader;

@Profile("dev")
@RestController
@RequestMapping("/api/egif")
public class EgifPreviewController {

    private final EgifXmlReader reader;
    private final Path archivo;

    public EgifPreviewController(
            EgifXmlReader reader,
            @Value("${forestguard.egif.input-file}") String archivo) {
        this.reader = reader;
        this.archivo = Path.of(archivo);
    }

    @GetMapping("/preview")
    public PreviewResponse previsualizar() throws Exception {
        List<IncendioEgif> incendios = reader.leer(archivo);

        return new PreviewResponse(
                incendios.size(),
                incendios.stream().limit(3).toList()
        );
    }

    public record PreviewResponse(
            int total,
            List<IncendioEgif> ejemplos
    ) {
    }
}