package io.forestguard.api.service;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.forestguard.api.client.OpenMeteoClient;
import io.forestguard.api.dto.MuestraResponse;
import io.forestguard.api.dto.OpenMeteoResponse;

@Service
public class ImportacionMeteorologicaService {

    private final OpenMeteoClient client;
    private final MuestraMeteorologicaService muestraService;
    
    public ImportacionMeteorologicaService(
            OpenMeteoClient client,
            MuestraMeteorologicaService muestraService) {

        this.client = client;
        this.muestraService = muestraService;
    }
    public MuestraResponse importarValencia() {

        OpenMeteoResponse respuesta = client.consultarValencia();

        if (respuesta == null
            || respuesta.actual() == null
            || respuesta.actual().time() == null
            || respuesta.actual().temperatura() == null
            || respuesta.timezone() == null
            || respuesta.timezone().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "La respuesta de Open-Meteo esta incompleta"
            );
        }

        OffsetDateTime instante;

        try {
            instante = respuesta.actual().time() // tiempo de respuesta de open meteo
                    .atZone(ZoneId.of(respuesta.timezone()))
                    .toOffsetDateTime();

        } catch (DateTimeException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "La zona horaria de Open-Meteo no es valida",
                    exception
            );
        }

        return muestraService.guardarDesdeModelo(
                instante,
                respuesta.actual().temperatura()
        );
    }
}
