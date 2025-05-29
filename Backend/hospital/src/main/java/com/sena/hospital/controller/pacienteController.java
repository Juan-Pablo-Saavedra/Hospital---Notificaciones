package com.sena.hospital.controller;

import com.sena.hospital.DTO.pacienteDTO;
import com.sena.hospital.service.pacienteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class pacienteController {

    @Autowired
    private pacienteService pacienteService;
    
    // Listar todos los pacientes
    @GetMapping
    public ResponseEntity<List<pacienteDTO>> getAll() {
        List<pacienteDTO> pacientes = pacienteService.getAllPacientes();
        return ResponseEntity.ok(pacientes);
    }
    
    // Obtener paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<pacienteDTO> getById(@PathVariable Long id) {
        pacienteDTO dto = pacienteService.getPacienteById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    
    // Crear nuevo paciente
    @PostMapping
    public ResponseEntity<pacienteDTO> create(@RequestBody pacienteDTO dto) {
        pacienteDTO created = pacienteService.createPaciente(dto);
        return ResponseEntity.ok(created);
    }
    
    // Actualizar paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<pacienteDTO> update(@PathVariable Long id, @RequestBody pacienteDTO dto) {
        pacienteDTO updated = pacienteService.updatePaciente(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
    
    // Eliminar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.ok().build();
    }
    
    // Cambiar el estado de suspensión de recordatorios para un paciente
    @PutMapping("/{id}/suspend")
    public ResponseEntity<pacienteDTO> suspend(@PathVariable Long id, @RequestParam boolean suspendidos) {
        pacienteDTO updated = pacienteService.setSuspension(id, suspendidos);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}
