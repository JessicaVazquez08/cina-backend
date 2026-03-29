package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Paciente;
import com.example.cinaBackend.service.PacienteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin("*")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Paciente> listar() {
        return service.listar();
    }

    @PostMapping
    public Paciente guardar(@RequestBody Paciente paciente) {
        return service.guardar(paciente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/buscar")
    public List<Paciente> buscar(@RequestParam String query) {
        return service.buscar(query);
    }

    @GetMapping("/{id}")
    public Paciente obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public Paciente actualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        return service.actualizar(id, paciente);
    }
}