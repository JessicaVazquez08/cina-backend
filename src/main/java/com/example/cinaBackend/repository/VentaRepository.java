
package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
