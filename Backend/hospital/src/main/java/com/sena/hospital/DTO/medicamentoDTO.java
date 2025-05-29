package com.sena.hospital.DTO;

public class medicamentoDTO {

    private Long id;
    private String nombre;
    private String dosis;      // Ejemplo: "500 mg"
    private String horarios;   // Formato CSV, por ejemplo: "08:00,14:00,20:00"

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
    public String getDosis() {
        return dosis;
    }
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }
    public String getHorarios() {
        return horarios;
    }
    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }
}
