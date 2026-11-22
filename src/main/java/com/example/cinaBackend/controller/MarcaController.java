
package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Marca;
import com.example.cinaBackend.service.MarcaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin("*")
public class MarcaController {

    private final MarcaService service;

    public MarcaController(MarcaService service) {
        this.service = service;
    }

    // =========================
    // LISTAR
    // =========================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Marca> listar() {
        return service.listar();
    }

    // =========================
    // CREAR
    // =========================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Marca guardar(@RequestBody Marca marca) {
        return service.guardar(marca);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Marca actualizar(@PathVariable Long id, @RequestBody Marca m) {
        return service.actualizar(id, m);
    }
}
