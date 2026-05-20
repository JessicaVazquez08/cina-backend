package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.Marca;
import com.example.cinaBackend.repository.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository repository;

    public MarcaService(MarcaRepository repository) {
        this.repository = repository;
    }

    // =========================
    // LISTAR
    // =========================
    public List<Marca> listar() {
        return repository.findAll();
    }

    // =========================
    // CREAR
    // =========================
    public Marca guardar(Marca marca) {

        if (marca.getNombre() == null || marca.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la marca es obligatorio");
        }

        repository.findByNombreIgnoreCase(marca.getNombre())
                .ifPresent(m -> {
                    throw new RuntimeException("La marca ya existe");
                });

        return repository.save(marca);
    }

    // =========================
    // ACTUALIZAR (opcional)
    // =========================
    public Marca actualizar(Long id, Marca datos) {

        Marca m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        m.setNombre(datos.getNombre());

        return repository.save(m);
    }
}