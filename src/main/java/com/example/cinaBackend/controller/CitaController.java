package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Cita;
import com.example.cinaBackend.service.CitaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin("*")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cita> listar() {
        return service.listar();
    }

    @PostMapping
    public Cita guardar(@RequestBody Cita cita) {
        return service.guardar(cita);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/fecha")
    public List<Cita> obtenerPorFecha(@RequestParam String fecha) {
        return service.buscarPorFecha(LocalDate.parse(fecha));
    }

    @GetMapping("/disponibles")
    public List<Cita> disponibles(@RequestParam String fecha) {
        return service.horariosDisponibles(LocalDate.parse(fecha));
    }

    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        return service.actualizar(id, cita);
    }
}