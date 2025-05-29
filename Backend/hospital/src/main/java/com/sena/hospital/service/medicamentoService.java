package com.sena.hospital.service;

import com.sena.hospital.DTO.medicamentoDTO;
import com.sena.hospital.model.medicamento;
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
    
    // Convierte la entidad medicamento a DTO
    private medicamentoDTO convertToDto(medicamento m) {
        medicamentoDTO dto = new medicamentoDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setDosis(m.getDosis());
        dto.setHorarios(m.getHorarios());
        return dto;
    }
    
    // Convierte el DTO a entidad medicamento (sin asignar ID)
    private medicamento convertToEntity(medicamentoDTO dto) {
        medicamento m = new medicamento();
        m.setNombre(dto.getNombre());
        m.setDosis(dto.getDosis());
        m.setHorarios(dto.getHorarios());
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
    
    // Actualiza el medicamento según el ID y el DTO
    public medicamentoDTO updateMedicamento(Long id, medicamentoDTO medicamentoDto) {
        Optional<medicamento> opt = medicamentoRepository.findById(id);
        if (opt.isPresent()) {
            medicamento m = opt.get();
            m.setNombre(medicamentoDto.getNombre());
            m.setDosis(medicamentoDto.getDosis());
            m.setHorarios(medicamentoDto.getHorarios());
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
