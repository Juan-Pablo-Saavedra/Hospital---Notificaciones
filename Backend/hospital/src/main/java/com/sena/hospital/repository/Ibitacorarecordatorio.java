package com.sena.hospital.repository;

import com.sena.hospital.model.bitacorarecordatorio;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Ibitacorarecordatorio extends JpaRepository<bitacorarecordatorio, Long> {
    
    // Obtiene el historial de recordatorios para un paciente dado su ID
    List<bitacorarecordatorio> findByPacienteId(Long pacienteId);
    
    // Consulta para obtener los recordatorios no confirmados que fueron enviados antes de una fecha dada
    @Query("SELECT br FROM bitacorarecordatorio br WHERE br.confirmado = false AND br.fechaEnvio <= :dateTime")
    List<bitacorarecordatorio> findUnconfirmedBefore(@Param("dateTime") LocalDateTime dateTime);
}
