package com.sena.hospital.service;

import com.sena.hospital.DTO.bitacorarecordatorioDTO;
import com.sena.hospital.model.bitacorarecordatorio;
import com.sena.hospital.repository.Ibitacorarecordatorio;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class bitacorarecordatorioService {

    @Autowired
    private Ibitacorarecordatorio bitacoraRepo;
    
    // Convierte la entidad a DTO
    private bitacorarecordatorioDTO convertToDto(bitacorarecordatorio br) {
        bitacorarecordatorioDTO dto = new bitacorarecordatorioDTO();
        dto.setId(br.getId());
        dto.setPacienteId(br.getPaciente().getId());
        dto.setMedicamentoId(br.getMedicamento().getId());
        dto.setFechaEnvio(br.getFechaEnvio());
        dto.setConfirmado(br.isConfirmado());
        return dto;
    }
    
    // Convierte el DTO a entidad
    private bitacorarecordatorio convertToEntity(bitacorarecordatorioDTO dto) {
        bitacorarecordatorio br = new bitacorarecordatorio();
        br.setFechaEnvio(dto.getFechaEnvio());
        br.setConfirmado(dto.isConfirmado());
        return br;
    }
    
    // Devuelve todos los registros convertidos a DTO
    public List<bitacorarecordatorioDTO> getAllRecordatorios() {
        return bitacoraRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Obtiene un registro de recordatorio por ID
    public bitacorarecordatorioDTO getRecordatorioById(Long id) {
        Optional<bitacorarecordatorio> opt = bitacoraRepo.findById(id);
        return opt.map(this::convertToDto).orElse(null);
    }
    
    // Crea un nuevo registro a partir del DTO
    public bitacorarecordatorioDTO createRecordatorio(bitacorarecordatorioDTO dto) {
        bitacorarecordatorio br = convertToEntity(dto);
        br = bitacoraRepo.save(br);
        return convertToDto(br);
    }
    
    // Actualiza un registro existente
    public bitacorarecordatorioDTO updateRecordatorio(Long id, bitacorarecordatorioDTO dto) {
        Optional<bitacorarecordatorio> opt = bitacoraRepo.findById(id);
        if (opt.isPresent()) {
            bitacorarecordatorio br = opt.get();
            br.setFechaEnvio(dto.getFechaEnvio());
            br.setConfirmado(dto.isConfirmado());
            br = bitacoraRepo.save(br);
            return convertToDto(br);
        }
        return null;
    }
    
    // Elimina un registro por su ID
    public void deleteRecordatorio(Long id) {
        bitacoraRepo.deleteById(id);
    }
    
    // Obtiene los registros de recordatorios para un paciente específico
    public List<bitacorarecordatorioDTO> getRecordatoriosByPacienteId(Long pacienteId) {
        return bitacoraRepo.findByPacienteId(pacienteId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Busca los recordatorios no confirmados enviados antes de una fecha dada
    public List<bitacorarecordatorioDTO> findUnconfirmedBefore(LocalDateTime dateTime) {
        return bitacoraRepo.findUnconfirmedBefore(dateTime).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Confirma un recordatorio dado su ID
    public bitacorarecordatorioDTO confirmRecordatorio(Long id) {
        Optional<bitacorarecordatorio> opt = bitacoraRepo.findById(id);
        if (opt.isPresent()) {
            bitacorarecordatorio br = opt.get();
            br.setConfirmado(true);
            br = bitacoraRepo.save(br);
            return convertToDto(br);
        }
        return null;
    }
}
