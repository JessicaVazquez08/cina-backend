package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.Cita;
import com.example.cinaBackend.entity.Paciente;
import com.example.cinaBackend.enums.EstadoCita;
import com.example.cinaBackend.repository.CitaRepository;
import com.example.cinaBackend.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class CitaService {

    private final CitaRepository repository;

    private final PacienteRepository pacienteRepository;

    public CitaService(CitaRepository repository, PacienteRepository pacienteRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Cita> listar() {
        return repository.findAll();
    }

    public Cita guardar(Cita cita) {

        if (cita.getPaciente() == null || cita.getPaciente().getId() == null) {
            throw new RuntimeException("Paciente obligatorio");
        }

        boolean existe = repository.existsByFechaAndHora(
                cita.getFecha(),
                cita.getHora()
        );

        if (existe) {
            throw new RuntimeException("Ya existe una cita en ese horario");
        }

        Paciente paciente = pacienteRepository
                .findById(cita.getPaciente().getId())
                .orElseThrow(() -> new RuntimeException("Paciente no existe"));

        cita.setPaciente(paciente);

        cita.setEstado(EstadoCita.OCUPADO);

        return repository.save(cita);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Cita> buscarPorFecha(LocalDate fecha) {
        return repository.findByFecha(fecha);
    }

    public List<Cita> horariosDisponibles(LocalDate fecha) {
        return repository.findByFechaAndEstado(fecha, EstadoCita.DISPONIBLE);
    }

    public Cita actualizar(Long id, Cita datos) {

        Cita cita = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setFecha(datos.getFecha());
        cita.setHora(datos.getHora());
        cita.setEstado(datos.getEstado());

        Paciente paciente = pacienteRepository
                .findById(datos.getPaciente().getId())
                .orElseThrow(() -> new RuntimeException("Paciente no existe"));

        cita.setPaciente(paciente);

        return repository.save(cita);
    }
}