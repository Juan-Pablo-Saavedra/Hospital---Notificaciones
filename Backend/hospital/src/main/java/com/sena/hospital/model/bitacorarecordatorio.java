package com.sena.hospital.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacorarecordatorio")
public class bitacorarecordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del paciente al que se envió el recordatorio
    @Column(nullable = false)
    private Long pacienteId;

    // ID del medicamento relacionado al recordatorio
    @Column(nullable = false)
    private Long medicamentoId;

    // Fecha y hora en que se envió el recordatorio
    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    // Indica si el recordatorio fue confirmado (por ejemplo, mediante la acción del paciente)
    @Column(nullable = false)
    private boolean confirmado;

    // Campo opcional para observaciones o mensajes del envío
    @Column
    private String observacion;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
