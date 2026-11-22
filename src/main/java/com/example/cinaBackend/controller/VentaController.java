package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Producto;
import com.example.cinaBackend.entity.Venta;
import com.example.cinaBackend.entity.VentaDetalle;
import com.example.cinaBackend.service.ProductoService;
import com.example.cinaBackend.service.VentaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.cinaBackend.dto.CompraRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin("*")
public class VentaController {

    private final VentaService service;
    private final ProductoService productoService;

    public VentaController(VentaService service, ProductoService productoService) {
        this.service = service;
        this.productoService = productoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Venta crear(@RequestBody List<VentaDetalle> detalles) {
        return service.crearVenta(detalles);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Producto> listar(@RequestParam(required = false) String search) {

        if (search == null || search.isEmpty()) {
            return productoService.listar();
        }

        return productoService.buscar(search);
    }

    @PostMapping("/compra")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Venta crearCompra(
            @RequestBody CompraRequest request,
            Authentication authentication
    ) {
        return service.crearCompra(
                request,
                authentication.getName()
        );
    }
}