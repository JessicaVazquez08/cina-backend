
package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.Producto;
import com.example.cinaBackend.entity.ProductoPrecio;
import com.example.cinaBackend.repository.ProductoRepository;
import com.example.cinaBackend.repository.ProductoPrecioRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final ProductoPrecioRepository precioRepository;
    private final ImagenProductoService imagenService;

    public ProductoService(
            ProductoRepository repository,
            ProductoPrecioRepository precioRepository,
            ImagenProductoService imagenService) {

        this.repository = repository;
        this.precioRepository = precioRepository;
        this.imagenService = imagenService;
    }

    public List<Producto> listar() {

        List<Producto> productos = repository.findAll();

        for (Producto p : productos) {
            p.setPrecioActual(calcularPrecioActual(p.getId()));
        }

        return productos;
    }

    // CREAR PRODUCTO SIN IMAGEN (COMPATIBILIDAD)
    public Producto guardar(Producto producto) {
        producto.setStock(0);
        return repository.save(producto);
    }

    // CREAR PRODUCTO CON IMAGEN
    public Producto guardar(
            Producto producto,
            MultipartFile imagen) {

        producto.setStock(0);

        if (imagen != null && !imagen.isEmpty()) {
            producto.setImagenUrl(imagenService.guardar(imagen));
        }

        return repository.save(producto);
    }

    // ACTUALIZAR PRODUCTO SIN CAMBIAR SU IMAGEN
    public Producto actualizar(Long id, Producto datos) {

        Producto p = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));

        p.setNombre(datos.getNombre());
        p.setDescripcion(datos.getDescripcion());
        p.setStockMinimo(datos.getStockMinimo());
        p.setMarca(datos.getMarca());

        return repository.save(p);
    }

    // ACTUALIZAR PRODUCTO Y SU IMAGEN
    public Producto actualizar(
            Long id,
            Producto datos,
            MultipartFile imagen) {

        Producto p = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));

        p.setNombre(datos.getNombre());
        p.setDescripcion(datos.getDescripcion());
        p.setStockMinimo(datos.getStockMinimo());
        p.setMarca(datos.getMarca());

        if (imagen != null && !imagen.isEmpty()) {
            p.setImagenUrl(imagenService.guardar(imagen));
        }

        return repository.save(p);
    }

    public List<Producto> buscar(String search) {

        List<Producto> productos =
                repository.findByNombreContainingIgnoreCaseOrMarca_NombreContainingIgnoreCase(
                        search, search
                );

        for (Producto p : productos) {
            p.setPrecioActual(calcularPrecioActual(p.getId()));
        }

        return productos;
    }

    public Double obtenerPrecioActual(Long productoId) {

        List<ProductoPrecio> precios =
                precioRepository.findByProductoIdOrderByFechaInicioDesc(
                        productoId
                );

        LocalDate hoy = LocalDate.now();

        for (ProductoPrecio p : precios) {

            if ((p.getFechaInicio().isBefore(hoy)
                    || p.getFechaInicio().isEqual(hoy))
                    && (p.getFechaFin() == null
                    || p.getFechaFin().isAfter(hoy))) {

                return p.getPrecioVenta();
            }
        }

        return 0.0;
    }

    public Double calcularPrecioActual(Long productoId) {

        return obtenerPrecioActual(productoId);
    }
}
