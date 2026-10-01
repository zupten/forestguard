package io.forestguard.api.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name = "muestras_meteorologicas")
public class MuestraMeteorologica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String estacion;
    
    @Column(nullable = false)
    private LocalDate fecha;
    
    @Column(nullable = false)
    private Double temperatura;
    
    @Column(nullable = false)
    private String origen;

    @Column(nullable = true)
    private OffsetDateTime instante;

    public MuestraMeteorologica() {
    }

    public Long getId() {
        return id;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
    

    public OffsetDateTime getInstante() {
        return instante;
    }

    public void setInstante(OffsetDateTime instante) {
        this.instante = instante;
    }
}