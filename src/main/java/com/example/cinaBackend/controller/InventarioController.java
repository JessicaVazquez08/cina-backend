
package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Lote;
import com.example.cinaBackend.entity.ProductoPrecio;
import com.example.cinaBackend.service.InventarioService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Lote crearLote(@RequestBody Lote lote) {
        return service.crearLote(lote);
    }

    // ===============================
    // LOTES ACTIVOS
    // ===============================
    @GetMapping("/lotes/{productoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Lote> obtenerLotes(@PathVariable Long productoId) {
        return service.obtenerLotesActivos(productoId);
    }

    // ===============================
    // PRECIOS
    // ===============================
    @PostMapping("/precios")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoPrecio crearPrecio(@RequestBody ProductoPrecio precio) {
        return service.crearPrecio(precio);
    }

    @GetMapping("/precios/{productoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductoPrecio> listarPrecios(@PathVariable Long productoId) {
        return service.listarPrecios(productoId);
    }

    // ===============================
    // SALIDA
    // ===============================
    @PostMapping("/salida")
    @PreAuthorize("hasRole('ADMIN')")
    public void salida(@RequestParam Long loteId, @RequestParam int cantidad) {
        service.registrarSalida(loteId, cantidad);
    }
}
