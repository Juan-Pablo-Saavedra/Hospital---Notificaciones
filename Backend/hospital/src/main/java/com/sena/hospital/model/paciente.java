package com.sena.hospital.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "paciente")
public class paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String telefono;

    private boolean recordatoriosSuspendidos = false;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<pacientemedicamento> medicamentos;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isRecordatoriosSuspendidos() {
        return recordatoriosSuspendidos;
    }

    public void setRecordatoriosSuspendidos(boolean recordatoriosSuspendidos) {
        this.recordatoriosSuspendidos = recordatoriosSuspendidos;
    }

    public List<pacientemedicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<pacientemedicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
}
