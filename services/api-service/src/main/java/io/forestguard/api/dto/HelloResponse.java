package io.forestguard.api.dto;

public record HelloResponse(
    String project,
    String service,
    String message
    ) {}
