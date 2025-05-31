package com.sena.hospital.controller;

import com.sena.hospital.DTO.bitacorarecordatorioDTO;
import com.sena.hospital.service.bitacorarecordatorioService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recordatorios")
public class bitacorarecordatorioController {

    @Autowired
    private bitacorarecordatorioService bitacoraService;
    
    // Listar todos los registros de recordatorios
    @GetMapping
    public ResponseEntity<List<bitacorarecordatorioDTO>> getAll() {
        List<bitacorarecordatorioDTO> list = bitacoraService.getAllRecordatorios();
        return ResponseEntity.ok(list);
    }
    
    // Obtener registro de recordatorio por ID
    @GetMapping("/{id}")
    public ResponseEntity<bitacorarecordatorioDTO> getById(@PathVariable Long id) {
        bitacorarecordatorioDTO dto = bitacoraService.getRecordatorioById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    
    // Crear un nuevo registro de recordatorio
    @PostMapping
    public ResponseEntity<bitacorarecordatorioDTO> create(@RequestBody bitacorarecordatorioDTO dto) {
        bitacorarecordatorioDTO created = bitacoraService.createRecordatorio(dto);
        return ResponseEntity.ok(created);
    }
    
    // Actualizar un registro existente
    @PutMapping("/{id}")
    public ResponseEntity<bitacorarecordatorioDTO> update(@PathVariable Long id, @RequestBody bitacorarecordatorioDTO dto) {
        bitacorarecordatorioDTO updated = bitacoraService.updateRecordatorio(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
    
    // Eliminar un registro de recordatorio
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        bitacoraService.deleteRecordatorio(id);
        return ResponseEntity.ok().build();
    }
    
    // Obtener recordatorios para un paciente específico
    @GetMapping("/byPaciente/{pacienteId}")
    public ResponseEntity<List<bitacorarecordatorioDTO>> getByPaciente(@PathVariable Long pacienteId) {
        List<bitacorarecordatorioDTO> list = bitacoraService.getRecordatoriosByPacienteId(pacienteId);
        return ResponseEntity.ok(list);
    }
    
    // Obtener los recordatorios no confirmados enviados antes de una fecha específica
    // Ejemplo: GET /api/recordatorios/unconfirmed?dateTime=2025-05-29T08:00:00
    @GetMapping("/unconfirmed")
    public ResponseEntity<List<bitacorarecordatorioDTO>> getUnconfirmed(@RequestParam("dateTime") String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        List<bitacorarecordatorioDTO> list = bitacoraService.findUnconfirmedBefore(dateTime);
        return ResponseEntity.ok(list);
    }
    
    // Confirmar un registro de recordatorio por su ID
    @PutMapping("/confirm/{id}")
    public ResponseEntity<bitacorarecordatorioDTO> confirm(@PathVariable Long id) {
        bitacorarecordatorioDTO confirmed = bitacoraService.confirmRecordatorio(id);
        if (confirmed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(confirmed);
    }
    
    // Nuevo endpoint: Confirmar recordatorios del día para un paciente y medicamento, mediante parámetros
    // Ejemplo: GET /api/recordatorios/confirmByParams?pacienteId=1&medicamentoId=2
    @GetMapping("/confirmByParams")
    public ResponseEntity<String> confirmByParams(@RequestParam Long pacienteId, @RequestParam Long medicamentoId) {
        bitacoraService.confirmRecordatoriosForToday(pacienteId, medicamentoId);
        return ResponseEntity.ok("Recordatorios confirmados para paciente: " + pacienteId + " y medicamento: " + medicamentoId);
    }
    
    // Nuevo endpoint: Obtener un contador de recordatorios enviados agrupados por paciente (clave = pacienteId, valor = cantidad)
    @GetMapping("/count")
    public ResponseEntity<Map<Long, Long>> countRecordatorios() {
        Map<Long, Long> counts = bitacoraService.countRecordatoriosByPaciente();
        return ResponseEntity.ok(counts);
    }
}
