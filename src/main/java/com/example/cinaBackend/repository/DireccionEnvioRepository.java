
package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.DireccionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DireccionEnvioRepository
        extends JpaRepository<DireccionEnvio, Long> {

    List<DireccionEnvio> findByUsuarioId(Long usuarioId);

    Optional<DireccionEnvio> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );
}
