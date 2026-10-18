package io.forestguard.egif.dto;

import java.time.LocalDateTime;

public record IncendioEgif(
        String numeroParte,
        String provincia,
        String municipio,
        LocalDateTime fechaDeteccion,
        Double latitud,
        Double longitud,
        String datumOrigen
) {
}