package com.sena.hospital.DTO;

public class pacienteDTO {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private boolean recordatoriosSuspendidos;

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
}
