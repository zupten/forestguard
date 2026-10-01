package io.forestguard.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoResponse(
        String timezone,
        @JsonProperty("current") DatosActuales actual
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DatosActuales(
            LocalDateTime time,
            @JsonProperty("temperature_2m") Double temperatura
    ) {
    }
}