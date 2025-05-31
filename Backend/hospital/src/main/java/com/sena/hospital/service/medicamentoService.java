package com.sena.hospital.service;

import com.sena.hospital.DTO.medicamentoDTO;
import com.sena.hospital.model.medicamento;
import com.sena.hospital.model.paciente;
import com.sena.hospital.repository.Imedicamento;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class medicamentoService {

    @Autowired
    private Imedicamento medicamentoRepository;
    
    // Convierte la entidad medicamento a DTO, incluyendo la información del paciente y el campo notificaciones.
    private medicamentoDTO convertToDto(medicamento m) {
        medicamentoDTO dto = new medicamentoDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setDosis(m.getDosis());
        dto.setHorarios(m.getHorarios());
        dto.setNotificaciones(m.isNotificaciones());
        if(m.getPaciente() != null) {
            dto.setPacienteId(m.getPaciente().getId());
            dto.setPacienteNombre(m.getPaciente().getNombre());
        }
        return dto;
    }
    
    // Convierte el DTO a entidad medicamento. Se asume que solo se envía el pacienteId,
    // por lo que se crea un objeto paciente con solo su id.
    private medicamento convertToEntity(medicamentoDTO dto) {
        medicamento m = new medicamento();
        m.setNombre(dto.getNombre());
        m.setDosis(dto.getDosis());
        m.setHorarios(dto.getHorarios());
        m.setNotificaciones(dto.isNotificaciones());
        if(dto.getPacienteId() != null) {
            paciente p = new paciente();
            p.setId(dto.getPacienteId());
            m.setPaciente(p);
        }
        return m;
    }
    
    // Lista todos los medicamentos convertidos a DTO
    public List<medicamentoDTO> getAllMedicamentos() {
        return medicamentoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Obtiene un medicamento por ID
    public medicamentoDTO getMedicamentoById(Long id) {
        Optional<medicamento> opt = medicamentoRepository.findById(id);
        return opt.map(this::convertToDto).orElse(null);
    }
    
    // Crea un nuevo medicamento a partir del DTO
    public medicamentoDTO createMedicamento(medicamentoDTO medicamentoDto) {
        medicamento m = convertToEntity(medicamentoDto);
        m = medicamentoRepository.save(m);
        return convertToDto(m);
    }
    
    // Actualiza un medicamento existente según el ID y el DTO
    public medicamentoDTO updateMedicamento(Long id, medicamentoDTO medicamentoDto) {
        Optional<medicamento> opt = medicamentoRepository.findById(id);
        if(opt.isPresent()){
            medicamento m = opt.get();
            m.setNombre(medicamentoDto.getNombre());
            m.setDosis(medicamentoDto.getDosis());
            m.setHorarios(medicamentoDto.getHorarios());
            m.setNotificaciones(medicamentoDto.isNotificaciones());
            if(medicamentoDto.getPacienteId() != null) {
                paciente p = new paciente();
                p.setId(medicamentoDto.getPacienteId());
                m.setPaciente(p);
            }
            m = medicamentoRepository.save(m);
            return convertToDto(m);
        }
        return null;
    }
    
    // Elimina un medicamento por ID
    public void deleteMedicamento(Long id) {
        medicamentoRepository.deleteById(id);
    }
}
