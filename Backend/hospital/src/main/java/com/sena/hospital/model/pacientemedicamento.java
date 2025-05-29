package com.sena.hospital.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacientemedicamento")
public class pacientemedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medicamento_id", nullable = false)
    private medicamento medicamento;

    // Fechas opcionales para el período de tratamiento
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(paciente paciente) {
        this.paciente = paciente;
    }

    public medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
}
