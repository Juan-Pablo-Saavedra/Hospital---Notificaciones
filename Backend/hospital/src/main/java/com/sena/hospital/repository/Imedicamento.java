package com.sena.hospital.repository;

import com.sena.hospital.model.medicamento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Imedicamento extends JpaRepository<medicamento, Long> {
    // Consulta para buscar un medicamento por su nombre
    Optional<medicamento> findByNombre(String nombre);
}
