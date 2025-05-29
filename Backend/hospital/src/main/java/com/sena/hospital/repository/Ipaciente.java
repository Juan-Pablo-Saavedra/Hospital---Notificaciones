package com.sena.hospital.repository;

import com.sena.hospital.model.paciente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Ipaciente extends JpaRepository<paciente, Long> {
    // Consulta para buscar un paciente mediante su correo
    Optional<paciente> findByEmail(String email);
}
