package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<MovimientoInventario, Long> {
}