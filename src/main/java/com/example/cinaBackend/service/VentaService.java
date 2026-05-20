package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.*;
import com.example.cinaBackend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final MovimientoRepository movimientoRepository;

    public VentaService(
            VentaRepository ventaRepository,
            ProductoRepository productoRepository,
            LoteRepository loteRepository,
            MovimientoRepository movimientoRepository
    ) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public Venta crearVenta(List<VentaDetalle> detalles) {

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setDetalles(new ArrayList<>());

        double total = 0;

        for (VentaDetalle d : detalles) {

            Long productoId = d.getProducto().getId();

            List<Lote> lotes = loteRepository
                    .findByProductoIdAndStockActualGreaterThanOrderByFechaEntradaAsc(productoId, 0);

            int cantidadPendiente = d.getCantidad();

            if (lotes.isEmpty()) {
                throw new RuntimeException("No hay stock disponible");
            }

            for (Lote lote : lotes) {

                if (cantidadPendiente == 0) break;

                int disponible = lote.getStockActual();

                int aDescontar = Math.min(disponible, cantidadPendiente);

                // descontar lote
                lote.setStockActual(disponible - aDescontar);
                loteRepository.save(lote);

                // movimiento
                MovimientoInventario mov = new MovimientoInventario();
                mov.setTipo("SALIDA");
                mov.setCantidad(aDescontar);
                mov.setFecha(LocalDate.now());
                mov.setProducto(lote.getProducto());
                mov.setLote(lote);
                mov.setMotivo("Venta FIFO");

                movimientoRepository.save(mov);

                // detalle por lote (importante)
                VentaDetalle detalleLote = new VentaDetalle();
                //detalleLote.setVenta(venta);
                detalleLote.setProducto(lote.getProducto());
                detalleLote.setLote(lote);
                detalleLote.setCantidad(aDescontar);
                detalleLote.setPrecioVenta(d.getPrecioVenta());

                venta.getDetalles().add(detalleLote);

                cantidadPendiente -= aDescontar;
            }

            if (cantidadPendiente > 0) {
                throw new RuntimeException("Stock insuficiente para cubrir la venta");
            }

            total += d.getCantidad() * d.getPrecioVenta();

            // actualizar stock total producto
            Producto producto = productoRepository.findById(productoId).orElseThrow();
            producto.setStock(producto.getStock() - d.getCantidad());
            productoRepository.save(producto);
        }

        venta.setTotal(total);

        return ventaRepository.save(venta);
    }
}