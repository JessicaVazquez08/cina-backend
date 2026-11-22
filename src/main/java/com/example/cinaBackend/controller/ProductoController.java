
package com.example.cinaBackend.controller;

import com.example.cinaBackend.entity.Producto;
import com.example.cinaBackend.service.ProductoService;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // =====================================
    // LISTAR Y BUSCAR PRODUCTOS
    // =====================================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Producto> listar(
            @RequestParam(required = false) String search) {

        if (search == null || search.trim().isEmpty()) {
            return service.listar();
        }

        return service.buscar(search);
    }

    // =====================================
    // CREAR PRODUCTO - JSON
    // =====================================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Producto guardar(@RequestBody Producto producto) {

        return service.guardar(producto);
    }

    // =====================================
    // CREAR PRODUCTO - MULTIPART
    // =====================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Producto guardarConImagen(
            @RequestPart("producto") Producto producto,
            @RequestPart(value = "imagen", required = false)
                    MultipartFile imagen) {

        return service.guardar(producto, imagen);
    }

    // =====================================
    // ACTUALIZAR PRODUCTO - JSON
    // =====================================
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public Producto actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        return service.actualizar(id, producto);
    }

    // =====================================
    // ACTUALIZAR PRODUCTO - MULTIPART
    // =====================================
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public Producto actualizarConImagen(
            @PathVariable Long id,
            @RequestPart("producto") Producto producto,
            @RequestPart(value = "imagen", required = false)
                    MultipartFile imagen) {

        return service.actualizar(id, producto, imagen);
    }
}
