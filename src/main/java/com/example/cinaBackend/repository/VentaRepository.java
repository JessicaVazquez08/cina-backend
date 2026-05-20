package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}