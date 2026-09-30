package io.forestguard.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearMuestraRequest(
        @NotBlank(message = "La estacion es obligatoria")
        String estacion,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La temperatura es obligatoria")
        Double temperatura,

        @NotBlank(message = "El origen es obligatorio")
        String origen
) {
}