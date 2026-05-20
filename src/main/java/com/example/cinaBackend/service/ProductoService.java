package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.Producto;
import com.example.cinaBackend.entity.ProductoPrecio;
import com.example.cinaBackend.repository.ProductoRepository;
import com.example.cinaBackend.repository.ProductoPrecioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final ProductoPrecioRepository precioRepository;

    public ProductoService(ProductoRepository repository, ProductoPrecioRepository precioRepository) {
        this.repository = repository;
        this.precioRepository = precioRepository;
    }

    public List<Producto> listar() {
        List<Producto> productos = repository.findAll();

        for (Producto p : productos) {
            p.setPrecioActual(calcularPrecioActual(p.getId()));
        }

        return productos;
    }

    public Producto guardar(Producto producto) {
        producto.setStock(0); // importante: no se define desde UI
        return repository.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {

        Producto p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        p.setNombre(datos.getNombre());
        p.setDescripcion(datos.getDescripcion());
        p.setStockMinimo(datos.getStockMinimo());
        p.setMarca(datos.getMarca());

        return repository.save(p);
    }

    public List<Producto> buscar(String search) {
        List<Producto> productos =
                repository.findByNombreContainingIgnoreCaseOrMarca_NombreContainingIgnoreCase(search, search);

        for (Producto p : productos) {
            p.setPrecioActual(calcularPrecioActual(p.getId()));
        }

        return productos;
    }

    public Double obtenerPrecioActual(Long productoId) {

        List<ProductoPrecio> precios =
                precioRepository.findByProductoIdOrderByFechaInicioDesc(productoId);

        LocalDate hoy = LocalDate.now();

        for (ProductoPrecio p : precios) {
            if ((p.getFechaInicio().isBefore(hoy) || p.getFechaInicio().isEqual(hoy)) &&
                    (p.getFechaFin() == null || p.getFechaFin().isAfter(hoy))) {

                return p.getPrecioVenta();
            }
        }

        return 0.0;
    }

    public Double calcularPrecioActual(Long productoId) {

        List<ProductoPrecio> precios =
                precioRepository.findByProductoIdOrderByFechaInicioDesc(productoId);

        LocalDate hoy = LocalDate.now();

        for (ProductoPrecio p : precios) {

            boolean inicioValido =
                    p.getFechaInicio().isBefore(hoy) || p.getFechaInicio().isEqual(hoy);

            boolean finValido =
                    p.getFechaFin() == null || p.getFechaFin().isAfter(hoy);

            if (inicioValido && finValido) {
                return p.getPrecioVenta();
            }
        }

        return 0.0;
    }
}