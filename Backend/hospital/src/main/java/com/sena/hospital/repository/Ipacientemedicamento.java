package com.sena.hospital.repository;

import com.sena.hospital.model.pacientemedicamento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Ipacientemedicamento extends JpaRepository<pacientemedicamento, Long> {
    
    // Obtiene todas las asignaciones de un paciente dado su ID
    List<pacientemedicamento> findByPacienteId(Long pacienteId);
    
    // Consulta para obtener las asignaciones activas en función del tiempo actual:
    // where fechaInicio <= now and (fechaFin is null or fechaFin >= now)
    @Query("SELECT pm FROM pacientemedicamento pm WHERE pm.fechaInicio <= :now AND (pm.fechaFin IS NULL OR pm.fechaFin >= :now)")
    List<pacientemedicamento> findActiveMedications(@Param("now") LocalDateTime now);
}
