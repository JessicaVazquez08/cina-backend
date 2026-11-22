
package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.DireccionEnvio;
import com.example.cinaBackend.service.DireccionEnvioService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/direcciones")
@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
public class DireccionEnvioController {

    private final DireccionEnvioService service;

    public DireccionEnvioController(DireccionEnvioService service) {
        this.service = service;
    }

    @GetMapping
    public List<DireccionEnvio> listar(Authentication authentication) {
        return service.listar(authentication.getName());
    }

    @PostMapping
    public DireccionEnvio crear(
            @RequestBody DireccionEnvio direccion,
            Authentication authentication
    ) {
        return service.crear(direccion, authentication.getName());
    }

    @PutMapping("/{id}")
    public DireccionEnvio actualizar(
            @PathVariable Long id,
            @RequestBody DireccionEnvio direccion,
            Authentication authentication
    ) {
        return service.actualizar(
                id,
                direccion,
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        service.eliminar(id, authentication.getName());
    }

    @PatchMapping("/{id}/predeterminada")
    public DireccionEnvio marcarPredeterminada(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return service.marcarPredeterminada(
                id,
                authentication.getName()
        );
    }
}
