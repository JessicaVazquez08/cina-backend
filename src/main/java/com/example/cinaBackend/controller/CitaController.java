package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Cita;
import com.example.cinaBackend.service.CitaService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Cita> listar() {
        return service.listar();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Cita guardar(@RequestBody Cita cita) {
        return service.guardar(cita);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/fecha")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Cita> obtenerPorFecha(@RequestParam String fecha) {
        return service.buscarPorFecha(LocalDate.parse(fecha));
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Cita> disponibles(@RequestParam String fecha) {
        return service.horariosDisponibles(LocalDate.parse(fecha));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        return service.actualizar(id, cita);
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Cita cancelar(@PathVariable Long id) {
        return service.cancelar(id);
    }

    @PutMapping("/{id}/atender")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Cita atender(@PathVariable Long id) {
        return service.marcarAtendida(id);
    }
}