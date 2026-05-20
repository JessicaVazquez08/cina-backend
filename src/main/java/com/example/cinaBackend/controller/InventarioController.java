package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Lote;
import com.example.cinaBackend.entity.ProductoPrecio;
import com.example.cinaBackend.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin("*")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    // ===============================
    // CREAR LOTE
    // ===============================
    @PostMapping("/lotes")
    public Lote crearLote(@RequestBody Lote lote) {
        return service.crearLote(lote);
    }

    // ===============================
    // LOTES ACTIVOS
    // ===============================
    @GetMapping("/lotes/{productoId}")
    public List<Lote> obtenerLotes(@PathVariable Long productoId) {
        return service.obtenerLotesActivos(productoId);
    }

    // ===============================
    // PRECIOS
    // ===============================
    @PostMapping("/precios")
    public ProductoPrecio crearPrecio(@RequestBody ProductoPrecio precio) {
        return service.crearPrecio(precio);
    }

    @GetMapping("/precios/{productoId}")
    public List<ProductoPrecio> listarPrecios(@PathVariable Long productoId) {
        return service.listarPrecios(productoId);
    }

    // ===============================
    // SALIDA
    // ===============================
    @PostMapping("/salida")
    public void salida(@RequestParam Long loteId, @RequestParam int cantidad) {
        service.registrarSalida(loteId, cantidad);
    }
}