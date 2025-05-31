package com.sena.hospital.controller;

import com.sena.hospital.DTO.medicamentoDTO;
import com.sena.hospital.service.medicamentoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicamentos")
public class medicamentoController {

    @Autowired
    private medicamentoService medicamentoService;
    
    // Listar todos los medicamentos
    @GetMapping
    public ResponseEntity<List<medicamentoDTO>> getAllMedicamentos() {
        List<medicamentoDTO> meds = medicamentoService.getAllMedicamentos();
        return ResponseEntity.ok(meds);
    }
    
    // Obtener medicamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<medicamentoDTO> getMedicamentoById(@PathVariable Long id) {
        medicamentoDTO dto = medicamentoService.getMedicamentoById(id);
        if(dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    
    // Crear nuevo medicamento
    @PostMapping
    public ResponseEntity<medicamentoDTO> createMedicamento(@RequestBody medicamentoDTO dto) {
        medicamentoDTO created = medicamentoService.createMedicamento(dto);
        return ResponseEntity.ok(created);
    }
    
    // Actualizar medicamento existente
    @PutMapping("/{id}")
    public ResponseEntity<medicamentoDTO> updateMedicamento(@PathVariable Long id, @RequestBody medicamentoDTO dto) {
        medicamentoDTO updated = medicamentoService.updateMedicamento(id, dto);
        if(updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
    
    // Eliminar medicamento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicamento(@PathVariable Long id) {
        medicamentoService.deleteMedicamento(id);
        return ResponseEntity.ok().build();
    }
}
