package io.forestguard.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.forestguard.api.entity.MuestraMeteorologica;

public interface MuestraMeteorologicaRepository
extends JpaRepository<MuestraMeteorologica, Long> {
}
