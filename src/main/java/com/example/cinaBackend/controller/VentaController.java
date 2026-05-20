package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Producto;
import com.example.cinaBackend.entity.Venta;
import com.example.cinaBackend.entity.VentaDetalle;
import com.example.cinaBackend.service.ProductoService;
import com.example.cinaBackend.service.VentaService;
import org.springframework.web.bind.annotation.*;

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
    public Venta crear(@RequestBody List<VentaDetalle> detalles) {
        return service.crearVenta(detalles);
    }

    @GetMapping
    public List<Producto> listar(@RequestParam(required = false) String search) {

        if (search == null || search.isEmpty()) {
            return productoService.listar();
        }

        return productoService.buscar(search);
    }
}