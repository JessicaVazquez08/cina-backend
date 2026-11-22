
package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.MetodoPago;
import com.example.cinaBackend.service.MetodoPagoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
public class MetodoPagoController {

    private final MetodoPagoService service;

    public MetodoPagoController(MetodoPagoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MetodoPago> listar(Authentication authentication) {
        return service.listar(authentication.getName());
    }

    @PostMapping
    public MetodoPago crear(
            @RequestBody MetodoPago metodo,
            Authentication authentication
    ) {
        return service.crear(metodo, authentication.getName());
    }

    @PutMapping("/{id}")
    public MetodoPago actualizar(
            @PathVariable Long id,
            @RequestBody MetodoPago metodo,
            Authentication authentication
    ) {
        return service.actualizar(
                id,
                metodo,
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

    @PatchMapping("/{id}/predeterminado")
    public MetodoPago marcarPredeterminado(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return service.marcarPredeterminado(
                id,
                authentication.getName()
        );
    }
}
