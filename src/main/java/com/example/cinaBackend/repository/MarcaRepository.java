package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Marca;
import com.example.cinaBackend.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNombreIgnoreCase(String nombre);

    interface MetodoPagoRepository
            extends JpaRepository<MetodoPago, Long> {

        List<MetodoPago> findByUsuarioId(Long usuarioId);

        Optional<MetodoPago> findByIdAndUsuarioId(
                Long id,
                Long usuarioId
        );
    }
}