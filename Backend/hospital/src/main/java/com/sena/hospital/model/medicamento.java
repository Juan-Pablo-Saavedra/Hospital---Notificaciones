package com.sena.hospital.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medicamento")
public class medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    private String dosis; // Ejemplo: "500 mg"
    
    // Relación ManyToOne con paciente (se asume que la entidad paciente ya está definida con al menos id y nombre)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private paciente paciente;

    @Column(name = "notificaciones", nullable = false)
    private boolean notificaciones;

    // Los horarios se almacenan en formato CSV, e.j.: "08:00,14:00,20:00"
    private String horarios;

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getDosis(){
        return dosis;
    }
    public void setDosis(String dosis){
        this.dosis = dosis;
    }
    
    public paciente getPaciente(){
        return paciente;
    }
    public void setPaciente(paciente paciente){
        this.paciente = paciente;
    }
    
    public boolean isNotificaciones(){
        return notificaciones;
    }
    public void setNotificaciones(boolean notificaciones){
        this.notificaciones = notificaciones;
    }
    
    public String getHorarios(){
        return horarios;
    }
    public void setHorarios(String horarios){
        this.horarios = horarios;
    }
}
