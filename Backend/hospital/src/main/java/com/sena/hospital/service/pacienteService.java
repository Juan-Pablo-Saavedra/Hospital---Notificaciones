package com.sena.hospital.service;

import com.sena.hospital.DTO.pacienteDTO;
import com.sena.hospital.model.paciente;
import com.sena.hospital.repository.Ipaciente;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class pacienteService {

    @Autowired
    private Ipaciente pacienteRepository;
    
    // Método privado para convertir la entidad 'paciente' al DTO 'pacienteDTO'
    private pacienteDTO convertToDto(paciente p) {
        pacienteDTO dto = new pacienteDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setEmail(p.getEmail());
        dto.setTelefono(p.getTelefono());
        dto.setRecordatoriosSuspendidos(p.isRecordatoriosSuspendidos());
        return dto;
    }
    
    // Método privado para convertir el DTO 'pacienteDTO' a la entidad 'paciente'
    // No se asigna el ID porque este lo genera la base de datos
    private paciente convertToEntity(pacienteDTO dto) {
        paciente p = new paciente();
        p.setNombre(dto.getNombre());
        p.setEmail(dto.getEmail());
        p.setTelefono(dto.getTelefono());
        p.setRecordatoriosSuspendidos(dto.isRecordatoriosSuspendidos());
        return p;
    }
    
    // Obtiene la lista de todos los pacientes y los convierte a DTO
    public List<pacienteDTO> getAllPacientes() {
        List<paciente> pacientes = pacienteRepository.findAll();
        return pacientes.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList());
    }
    
    // Obtiene un paciente por ID y lo convierte a DTO
    public pacienteDTO getPacienteById(Long id) {
        Optional<paciente> opt = pacienteRepository.findById(id);
        return opt.map(this::convertToDto).orElse(null);
    }
    
    // Crea un nuevo paciente con los datos proporcionados en el DTO
    public pacienteDTO createPaciente(pacienteDTO pacienteDto) {
        paciente p = convertToEntity(pacienteDto);
        p = pacienteRepository.save(p);
        return convertToDto(p);
    }
    
    // Actualiza un paciente existente según su ID y los datos proporcionados en el DTO
    public pacienteDTO updatePaciente(Long id, pacienteDTO pacienteDto) {
        Optional<paciente> opt = pacienteRepository.findById(id);
        if (opt.isPresent()) {
            paciente p = opt.get();
            p.setNombre(pacienteDto.getNombre());
            p.setEmail(pacienteDto.getEmail());
            p.setTelefono(pacienteDto.getTelefono());
            p.setRecordatoriosSuspendidos(pacienteDto.isRecordatoriosSuspendidos());
            p = pacienteRepository.save(p);
            return convertToDto(p);
        }
        return null;
    }
    
    // Elimina un paciente según su ID
    public void deletePaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
    
    // Cambia el estado de suspensión de los recordatorios para un paciente
    public pacienteDTO setSuspension(Long id, boolean suspendidos) {
        Optional<paciente> opt = pacienteRepository.findById(id);
        if (opt.isPresent()) {
            paciente p = opt.get();
            p.setRecordatoriosSuspendidos(suspendidos);
            p = pacienteRepository.save(p);
            return convertToDto(p);
        }
        return null;
    }
}
