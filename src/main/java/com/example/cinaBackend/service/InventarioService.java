package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.*;
import com.example.cinaBackend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventarioService {

    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;
    private final ProductoPrecioRepository precioRepository;
    private final MovimientoRepository movimientoRepository;

    public InventarioService(
            LoteRepository loteRepository,
            ProductoRepository productoRepository,
            ProductoPrecioRepository precioRepository,
            MovimientoRepository movimientoRepository
    ) {
        this.loteRepository = loteRepository;
        this.productoRepository = productoRepository;
        this.precioRepository = precioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    // ===============================
    // CREAR LOTE
    // ===============================
    public Lote crearLote(Lote lote) {

        lote.setStockActual(lote.getStockInicial());
        lote.setFechaEntrada(LocalDate.now());

        Lote saved = loteRepository.save(lote);

        Producto producto = productoRepository.findById(lote.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStock(producto.getStock() + lote.getStockInicial());
        productoRepository.save(producto);

        // movimiento
        MovimientoInventario mov = new MovimientoInventario();
        mov.setTipo("ENTRADA");
        mov.setCantidad(lote.getStockInicial());
        mov.setFecha(LocalDate.now());
        mov.setProducto(producto);
        mov.setLote(saved);
        mov.setMotivo("Alta de lote");

        movimientoRepository.save(mov);

        return saved;
    }

    // ===============================
    // LOTES ACTIVOS
    // ===============================
    public List<Lote> obtenerLotesActivos(Long productoId) {
        return loteRepository.findByProductoIdAndStockActualGreaterThan(productoId, 0);
    }

    // ===============================
    // PRECIOS
    // ===============================
    public ProductoPrecio crearPrecio(ProductoPrecio precio) {
        return precioRepository.save(precio);
    }

    public List<ProductoPrecio> listarPrecios(Long productoId) {
        return precioRepository.findByProductoIdOrderByFechaInicioDesc(productoId);
    }

    // ===============================
    // SALIDA (para ventas)
    // ===============================
    public void registrarSalida(Long loteId, int cantidad) {

        Lote lote = loteRepository.findById(loteId)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        if (lote.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        lote.setStockActual(lote.getStockActual() - cantidad);
        loteRepository.save(lote);

        Producto producto = lote.getProducto();
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        MovimientoInventario mov = new MovimientoInventario();
        mov.setTipo("SALIDA");
        mov.setCantidad(cantidad);
        mov.setFecha(LocalDate.now());
        mov.setProducto(producto);
        mov.setLote(lote);
        mov.setMotivo("Venta");

        movimientoRepository.save(mov);
    }
}
