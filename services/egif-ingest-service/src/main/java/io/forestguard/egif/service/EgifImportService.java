package io.forestguard.egif.service;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.forestguard.egif.dto.IncendioEgif;
import io.forestguard.egif.reader.EgifXmlReader;
import tools.jackson.databind.json.JsonMapper;

@Service
public class EgifImportService {

    private static final Logger log =
            LoggerFactory.getLogger(EgifImportService.class);

    private final EgifXmlReader reader;
    private final KafkaTemplate<String, String> kafka;
    private final JsonMapper jsonMapper;
    private final Path archivo;
    private final String topic;

    public EgifImportService(
            EgifXmlReader reader,
            KafkaTemplate<String, String> kafka,
            JsonMapper jsonMapper,
            @Value("${forestguard.egif.input-file}") String archivo,
            @Value("${forestguard.egif.topic}") String topic) {
        this.reader = reader;
        this.kafka = kafka;
        this.jsonMapper = jsonMapper;
        this.archivo = Path.of(archivo);
        this.topic = topic;
    }

    public synchronized ImportResult importar() throws Exception {
        // Validar y serializar todo el archivo antes del primer envio.
        List<IncendioEgif> incendios = reader.leer(archivo);
        List<String> mensajes = incendios.stream()
                .map(jsonMapper::writeValueAsString)
                .toList();

        int confirmados = 0;

        for (int i = 0; i < incendios.size(); i++) {
            String numero = incendios.get(i).numeroParte();

            try {
                // Contar solo los envios confirmados por Kafka.
                kafka.send(topic, numero, mensajes.get(i))
                        .get(30, TimeUnit.SECONDS);
                confirmados++;
            } catch (Exception exception) {
                if (exception instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }

                log.error(
                        "Importacion interrumpida. Confirmados={}, parte={}",
                        confirmados, numero, exception);

                throw new IllegalStateException(
                        "Importacion incompleta. Confirmados: " + confirmados
                        + ". Parte sin confirmacion: " + numero,
                        exception);
            }
        }

        log.info("Importacion completada: {} mensajes en {}", confirmados, topic);

        return new ImportResult(incendios.size(), confirmados, topic);
    }

    public record ImportResult(
            int leidos,
            int confirmados,
            String topic
    ) {
    }
}