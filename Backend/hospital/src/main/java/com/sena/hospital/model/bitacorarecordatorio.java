package com.sena.hospital.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacorarecordatorio")
public class bitacorarecordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medicamento_id", nullable = false)
    private medicamento medicamento;
    
    // Representa la fecha y hora en que se envió el recordatorio
    private LocalDateTime fechaEnvio;
    
    // Indica si el paciente confirmó la toma del medicamento
    private boolean confirmado;

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

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }
}
// Este modelo representa un registro de recordatorio enviado a un paciente para tomar su medicamento.