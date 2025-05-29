package com.sena.hospital.service;

import com.sena.hospital.DTO.pacientemedicamentoDTO;
import com.sena.hospital.model.pacientemedicamento;
import com.sena.hospital.repository.Ipacientemedicamento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class pacientemedicamentoService {

    @Autowired
    private Ipacientemedicamento pacMedRepository;
    
    // Convierte la entidad a DTO
    private pacientemedicamentoDTO convertToDto(pacientemedicamento pm) {
        pacientemedicamentoDTO dto = new pacientemedicamentoDTO();
        dto.setId(pm.getId());
        dto.setPacienteId(pm.getPaciente().getId());
        dto.setMedicamentoId(pm.getMedicamento().getId());
        dto.setFechaInicio(pm.getFechaInicio());
        dto.setFechaFin(pm.getFechaFin());
        return dto;
    }
    
    // Convierte el DTO a entidad (solo asigna fechas; se deben asignar las relaciones externamente)
    private pacientemedicamento convertToEntity(pacientemedicamentoDTO dto) {
        pacientemedicamento pm = new pacientemedicamento();
        pm.setFechaInicio(dto.getFechaInicio());
        pm.setFechaFin(dto.getFechaFin());
        return pm;
    }
    
    // Devuelve todas las asignaciones convertidas a DTO
    public List<pacientemedicamentoDTO> getAllPacientemedicamentos() {
        return pacMedRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Obtiene una asignación por ID
    public pacientemedicamentoDTO getPacientemedicamentoById(Long id) {
        Optional<pacientemedicamento> opt = pacMedRepository.findById(id);
        return opt.map(this::convertToDto).orElse(null);
    }
    
    // Crea una asignación a partir del DTO (las relaciones deben asignarse antes o mediante lógica adicional)
    public pacientemedicamentoDTO createPacientemedicamento(pacientemedicamentoDTO dto) {
        pacientemedicamento pm = convertToEntity(dto);
        pm = pacMedRepository.save(pm);
        return convertToDto(pm);
    }
    
    // Actualiza una asignación existente
    public pacientemedicamentoDTO updatePacientemedicamento(Long id, pacientemedicamentoDTO dto) {
        Optional<pacientemedicamento> opt = pacMedRepository.findById(id);
        if (opt.isPresent()) {
            pacientemedicamento pm = opt.get();
            pm.setFechaInicio(dto.getFechaInicio());
            pm.setFechaFin(dto.getFechaFin());
            pm = pacMedRepository.save(pm);
            return convertToDto(pm);
        }
        return null;
    }
    
    // Elimina la asignación por ID
    public void deletePacientemedicamento(Long id) {
        pacMedRepository.deleteById(id);
    }
    
    // Devuelve las asignaciones activas en el momento actual
    public List<pacientemedicamentoDTO> getActiveMedicationsForNow() {
        LocalDateTime now = LocalDateTime.now();
        return pacMedRepository.findActiveMedications(now).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Devuelve las asignaciones correspondientes a un paciente
    public List<pacientemedicamentoDTO> getMedicationsByPacienteId(Long pacienteId) {
        return pacMedRepository.findByPacienteId(pacienteId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
