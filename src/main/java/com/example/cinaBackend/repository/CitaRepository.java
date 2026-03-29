package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Cita;
import com.example.cinaBackend.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface CitaRepository extends JpaRepository<Cita, Long> {
    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);
    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByFechaAndEstado(LocalDate fecha, EstadoCita estado);
}
