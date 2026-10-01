package io.forestguard.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record MuestraResponse(
        Long id,
        String estacion,
        LocalDate fecha,
        Double temperatura,
        String origen,
        OffsetDateTime instante
) {
}