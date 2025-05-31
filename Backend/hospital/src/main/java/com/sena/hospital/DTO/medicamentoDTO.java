package com.sena.hospital.DTO;

public class medicamentoDTO {

    private Long id;
    private String nombre;
    private String dosis;      // Ejemplo: "500 mg"
    private String horarios;   // Formato CSV, por ejemplo: "08:00,14:00,20:00"
    
    // Campo para notificaciones (si se deben enviar recordatorios por correo)
    private boolean notificaciones;
    
    // Campos para la asociación con paciente
    private Long pacienteId;
    private String pacienteNombre;

    // Getters y Setters
    public Long getId(){
        return id;
    }
    public void setId(Long id){
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
    
    public String getHorarios(){
        return horarios;
    }
    public void setHorarios(String horarios){
        this.horarios = horarios;
    }
    
    public boolean isNotificaciones(){
        return notificaciones;
    }
    public void setNotificaciones(boolean notificaciones){
        this.notificaciones = notificaciones;
    }
    
    public Long getPacienteId(){
        return pacienteId;
    }
    public void setPacienteId(Long pacienteId){
        this.pacienteId = pacienteId;
    }
    
    public String getPacienteNombre(){
        return pacienteNombre;
    }
    public void setPacienteNombre(String pacienteNombre){
        this.pacienteNombre = pacienteNombre;
    }
}
