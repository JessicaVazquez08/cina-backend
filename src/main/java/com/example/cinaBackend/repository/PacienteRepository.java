package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNombreContainingIgnoreCaseOrTelefonoContainingOrEmailContainingIgnoreCase(
            String nombre, String telefono, String email
    );

    Optional<Paciente> findByEmail(String email);
}