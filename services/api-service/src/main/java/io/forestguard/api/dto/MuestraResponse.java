package io.forestguard.api.dto;

import java.time.LocalDate;

public record MuestraResponse(
        Long id,
        String estacion,
        LocalDate fecha,
        Double temperatura,
        String origen
) {
}