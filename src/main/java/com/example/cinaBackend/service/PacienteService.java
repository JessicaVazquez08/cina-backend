package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.Paciente;
import com.example.cinaBackend.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public List<Paciente> listar() {
        return repository.findAll();
    }

    public Paciente guardar(Paciente paciente) {
        Optional<Paciente> existente = repository.findByEmail(paciente.getEmail());

        if (existente.isPresent()) {
            return existente.get();
        }

        return repository.save(paciente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Paciente> buscar(String query) {
        return repository
                .findByNombreContainingIgnoreCaseOrTelefonoContainingOrEmailContainingIgnoreCase(
                        query, query, query
                );
    }

    public Paciente obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    public Paciente actualizar(Long id, Paciente datos) {

        Paciente p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        p.setNombre(datos.getNombre());
        p.setTelefono(datos.getTelefono());
        p.setEmail(datos.getEmail());

        return repository.save(p);
    }
}
