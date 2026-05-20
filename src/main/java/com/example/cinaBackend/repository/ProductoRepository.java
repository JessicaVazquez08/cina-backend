package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCaseOrMarca_NombreContainingIgnoreCase(
            String nombre,
            String marca
    );
}