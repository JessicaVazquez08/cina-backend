package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Long> {

    List<Lote> findByProductoIdAndStockActualGreaterThan(Long productoId, Integer stock);
    List<Lote> findByProductoIdAndStockActualGreaterThanOrderByFechaEntradaAsc(
            Long productoId,
            Integer stock
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT l FROM Lote l
    WHERE l.producto.id = :productoId
      AND l.stockActual > 0
    ORDER BY l.fechaEntrada ASC
""")
    List<Lote> buscarLotesDisponiblesParaVenta(
            @Param("productoId") Long productoId
    );
}