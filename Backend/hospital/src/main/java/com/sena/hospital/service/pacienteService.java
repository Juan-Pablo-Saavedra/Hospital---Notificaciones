package com.sena.hospital.service;

import com.sena.hospital.DTO.pacienteDTO;
import com.sena.hospital.model.paciente;
import com.sena.hospital.repository.Ipaciente;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class pacienteService {

    @Autowired
    private Ipaciente pacienteRepository;
    
    // Convierte la entidad 'paciente' al DTO 'pacienteDTO'
    private pacienteDTO convertToDto(paciente p) {
        pacienteDTO dto = new pacienteDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setEmail(p.getEmail());
        dto.setTelefono(p.getTelefono());
        dto.setRecordatoriosSuspendidos(p.isRecordatoriosSuspendidos());
        return dto;
    }
    
    // Convierte el DTO 'pacienteDTO' a la entidad 'paciente'
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
        try {
            paciente p = convertToEntity(pacienteDto);
            p = pacienteRepository.save(p);
            return convertToDto(p);
        } catch (DataIntegrityViolationException e) {
            // Mostrar mensaje minimal en consola
            System.err.println("Error: Duplicate entry for email or invalid phone format.");
            // Lanzar una excepción con mensaje amigable para el usuario
            throw new RuntimeException("El correo ya existe o el teléfono no cumple el formato.");
        } catch (Exception e) {
            System.err.println("Error creando paciente: " + e.getMessage());
            throw new RuntimeException("Error realizando el registro del paciente.");
        }
    }
    
    // Actualiza un paciente existente seg\u00FAn su ID y los datos proporcionados en el DTO
    public pacienteDTO updatePaciente(Long id, pacienteDTO pacienteDto) {
        try {
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
        } catch (DataIntegrityViolationException e) {
            System.err.println("Error: Duplicate entry for email or invalid phone format.");
            throw new RuntimeException("El correo ya existe o el teléfono no cumple el formato.");
        } catch (Exception e) {
            System.err.println("Error actualizando paciente: " + e.getMessage());
            throw new RuntimeException("Error actualizando el paciente.");
        }
    }
    
    // Elimina un paciente seg\u00FAn su ID
    public void deletePaciente(Long id) {
        try {
            pacienteRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error eliminando paciente: " + e.getMessage());
            throw new RuntimeException("Error eliminando el paciente.");
        }
    }
    
    // Cambia el estado de suspensi\u00F3n de los recordatorios para un paciente
    public pacienteDTO setSuspension(Long id, boolean suspendidos) {
        try {
            Optional<paciente> opt = pacienteRepository.findById(id);
            if (opt.isPresent()) {
                paciente p = opt.get();
                p.setRecordatoriosSuspendidos(suspendidos);
                p = pacienteRepository.save(p);
                return convertToDto(p);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error actualizando el estado de recordatorios: " + e.getMessage());
            throw new RuntimeException("Error actualizando el estado de recordatorios.");
        }
    }
}
