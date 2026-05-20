package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Long> {

    List<Lote> findByProductoIdAndStockActualGreaterThan(Long productoId, Integer stock);
    List<Lote> findByProductoIdAndStockActualGreaterThanOrderByFechaEntradaAsc(
            Long productoId,
            Integer stock
    );
}