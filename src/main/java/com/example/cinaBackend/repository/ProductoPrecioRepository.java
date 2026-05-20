package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.ProductoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoPrecioRepository extends JpaRepository<ProductoPrecio, Long> {

    List<ProductoPrecio> findByProductoIdOrderByFechaInicioDesc(Long productoId);
}
