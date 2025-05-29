package com.sena.hospital.controller;

import com.sena.hospital.DTO.pacientemedicamentoDTO;
import com.sena.hospital.service.pacientemedicamentoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paciente-medicamentos")
public class pacientemedicamentoController {

    @Autowired
    private pacientemedicamentoService pacMedService;
    
    // Listar todas las asignaciones
    @GetMapping
    public ResponseEntity<List<pacientemedicamentoDTO>> getAll() {
        List<pacientemedicamentoDTO> list = pacMedService.getAllPacientemedicamentos();
        return ResponseEntity.ok(list);
    }
    
    // Obtener asignación por ID
    @GetMapping("/{id}")
    public ResponseEntity<pacientemedicamentoDTO> getById(@PathVariable Long id) {
        pacientemedicamentoDTO dto = pacMedService.getPacientemedicamentoById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    
    // Crear nueva asignación
    @PostMapping
    public ResponseEntity<pacientemedicamentoDTO> create(@RequestBody pacientemedicamentoDTO dto) {
        pacientemedicamentoDTO created = pacMedService.createPacientemedicamento(dto);
        return ResponseEntity.ok(created);
    }
    
    // Actualizar asignación
    @PutMapping("/{id}")
    public ResponseEntity<pacientemedicamentoDTO> update(@PathVariable Long id, @RequestBody pacientemedicamentoDTO dto) {
        pacientemedicamentoDTO updated = pacMedService.updatePacientemedicamento(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
    
    // Eliminar asignación
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        pacMedService.deletePacientemedicamento(id);
        return ResponseEntity.ok().build();
    }
    
    // Listar asignaciones activas (donde la fecha actual está entre fechaInicio y fechaFin)
    @GetMapping("/active")
    public ResponseEntity<List<pacientemedicamentoDTO>> getActive() {
        List<pacientemedicamentoDTO> list = pacMedService.getActiveMedicationsForNow();
        return ResponseEntity.ok(list);
    }
    
    // Listar asignaciones de medicamentos para un paciente específico
    @GetMapping("/byPaciente/{pacienteId}")
    public ResponseEntity<List<pacientemedicamentoDTO>> getByPaciente(@PathVariable Long pacienteId) {
        List<pacientemedicamentoDTO> list = pacMedService.getMedicationsByPacienteId(pacienteId);
        return ResponseEntity.ok(list);
    }
}
