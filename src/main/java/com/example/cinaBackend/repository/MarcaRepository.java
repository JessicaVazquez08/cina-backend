package com.example.cinaBackend.repository;

import com.example.cinaBackend.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNombreIgnoreCase(String nombre);
}