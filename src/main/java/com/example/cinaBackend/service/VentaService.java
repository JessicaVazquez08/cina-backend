
package com.example.cinaBackend.service;

import com.example.cinaBackend.dto.CompraRequest;
import com.example.cinaBackend.dto.DetalleCompraRequest;
import com.example.cinaBackend.entity.*;
import com.example.cinaBackend.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final MovimientoRepository movimientoRepository;
    private final UserRepository userRepository;
    private final DireccionEnvioRepository direccionRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final ProductoService productoService;

    public VentaService(
            VentaRepository ventaRepository,
            ProductoRepository productoRepository,
            LoteRepository loteRepository,
            MovimientoRepository movimientoRepository,
            UserRepository userRepository,
            DireccionEnvioRepository direccionRepository,
            MetodoPagoRepository metodoPagoRepository,
            ProductoService productoService
    ) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
        this.movimientoRepository = movimientoRepository;
        this.userRepository = userRepository;
        this.direccionRepository = direccionRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.productoService = productoService;
    }

    // =====================================================
    // VENTA ADMINISTRATIVA EXISTENTE
    // =====================================================

    @Transactional
    public Venta crearVenta(List<VentaDetalle> detalles) {

        if (detalles == null || detalles.isEmpty()) {
            throw new RuntimeException("La venta no contiene productos");
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setDetalles(new ArrayList<>());

        double total = 0;

        for (VentaDetalle d : detalles) {

            if (d.getProducto() == null ||
                    d.getProducto().getId() == null ||
                    d.getCantidad() == null ||
                    d.getCantidad() <= 0) {
                throw new RuntimeException("Detalle de venta inválido");
            }

            Long productoId = d.getProducto().getId();

            List<Lote> lotes = loteRepository
                    .findByProductoIdAndStockActualGreaterThanOrderByFechaEntradaAsc(
                            productoId, 0
                    );

            int cantidadPendiente = d.getCantidad();

            if (lotes.isEmpty()) {
                throw new RuntimeException("No hay stock disponible");
            }

            for (Lote lote : lotes) {

                if (cantidadPendiente == 0) break;

                int disponible = lote.getStockActual();
                int aDescontar = Math.min(disponible, cantidadPendiente);

                lote.setStockActual(disponible - aDescontar);
                loteRepository.save(lote);

                registrarMovimiento(lote, aDescontar, "Venta FIFO");

                VentaDetalle detalleLote = new VentaDetalle();
                detalleLote.setVenta(venta);
                detalleLote.setProducto(lote.getProducto());
                detalleLote.setLote(lote);
                detalleLote.setCantidad(aDescontar);
                detalleLote.setPrecioVenta(d.getPrecioVenta());

                venta.getDetalles().add(detalleLote);

                cantidadPendiente -= aDescontar;
            }

            if (cantidadPendiente > 0) {
                throw new RuntimeException(
                        "Stock insuficiente para cubrir la venta"
                );
            }

            total += d.getCantidad() * d.getPrecioVenta();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() ->
                            new RuntimeException("Producto no encontrado")
                    );

            producto.setStock(producto.getStock() - d.getCantidad());
            productoRepository.save(producto);
        }

        venta.setTotal(total);

        return ventaRepository.save(venta);
    }

    // =====================================================
    // COMPRA DESDE EL CATÁLOGO
    // =====================================================

    @Transactional
    public Venta crearCompra(CompraRequest request, String email) {

        if (request == null ||
                request.getDetalles() == null ||
                request.getDetalles().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        DireccionEnvio direccion = direccionRepository
                .findByIdAndUsuarioId(
                        request.getDireccionId(),
                        usuario.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "La dirección seleccionada no existe"
                        )
                );

        MetodoPago metodoPago = metodoPagoRepository
                .findByIdAndUsuarioId(
                        request.getMetodoPagoId(),
                        usuario.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "El método de pago seleccionado no existe"
                        )
                );

        // Evitar productos repetidos en la misma solicitud.
        Set<Long> productosIncluidos = new HashSet<>();

        for (DetalleCompraRequest detalle : request.getDetalles()) {

            if (detalle.getProductoId() == null ||
                    detalle.getCantidad() == null ||
                    detalle.getCantidad() <= 0) {
                throw new RuntimeException(
                        "Los datos de los productos son inválidos"
                );
            }

            if (!productosIncluidos.add(detalle.getProductoId())) {
                throw new RuntimeException(
                        "Hay productos duplicados en la solicitud"
                );
            }
        }

        Venta venta = new Venta();

        venta.setFecha(LocalDateTime.now());
        venta.setUsuario(usuario);
        venta.setDetalles(new ArrayList<>());

        // Copia histórica de la dirección.
        venta.setNombreDestinatario(direccion.getNombreDestinatario());
        venta.setTelefonoEnvio(direccion.getTelefono());
        venta.setCalleEnvio(direccion.getCalle());
        venta.setNumeroExteriorEnvio(direccion.getNumeroExterior());
        venta.setNumeroInteriorEnvio(direccion.getNumeroInterior());
        venta.setColoniaEnvio(direccion.getColonia());
        venta.setCodigoPostalEnvio(direccion.getCodigoPostal());
        venta.setCiudadEnvio(direccion.getCiudad());
        venta.setEstadoEnvio(direccion.getEstado());
        venta.setReferenciasEnvio(direccion.getReferencias());

        // Copia histórica del método de pago.
        venta.setTipoTarjeta(metodoPago.getTipoTarjeta());
        venta.setUltimosCuatroTarjeta(metodoPago.getUltimosCuatro());

        double total = 0;

        for (DetalleCompraRequest detalle : request.getDetalles()) {

            Long productoId = detalle.getProductoId();
            int cantidadPendiente = detalle.getCantidad();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Producto no encontrado: " + productoId
                            )
                    );

            Double precio = productoService.obtenerPrecioActual(productoId);

            if (precio == null || precio <= 0) {
                throw new RuntimeException(
                        "El producto no tiene un precio vigente: "
                                + producto.getNombre()
                );
            }

            // Consulta FIFO con bloqueo de los lotes.
            List<Lote> lotes = loteRepository
                    .buscarLotesDisponiblesParaVenta(productoId);

            int stockDisponible = lotes.stream()
                    .mapToInt(Lote::getStockActual)
                    .sum();

            if (stockDisponible < cantidadPendiente) {
                throw new RuntimeException(
                        "Stock insuficiente para el producto: "
                                + producto.getNombre()
                );
            }

            for (Lote lote : lotes) {

                if (cantidadPendiente == 0) break;

                int disponible = lote.getStockActual();

                int aDescontar = Math.min(
                        disponible,
                        cantidadPendiente
                );

                // Descontar existencias del lote.
                lote.setStockActual(disponible - aDescontar);
                loteRepository.save(lote);

                // Registrar movimiento de inventario.
                registrarMovimiento(
                        lote,
                        aDescontar,
                        "Compra de catálogo FIFO"
                );

                // Registrar detalle relacionado con su venta.
                VentaDetalle detalleLote = new VentaDetalle();

                detalleLote.setVenta(venta);
                detalleLote.setProducto(producto);
                detalleLote.setLote(lote);
                detalleLote.setCantidad(aDescontar);
                detalleLote.setPrecioVenta(precio);

                venta.getDetalles().add(detalleLote);

                cantidadPendiente -= aDescontar;
            }

            // Actualizar stock total del producto.
            producto.setStock(
                    producto.getStock() - detalle.getCantidad()
            );

            productoRepository.save(producto);

            total += detalle.getCantidad() * precio;
        }

        venta.setTotal(total);

        return ventaRepository.save(venta);
    }

    // =====================================================
    // REGISTRO DE MOVIMIENTOS
    // =====================================================

    private void registrarMovimiento(
            Lote lote,
            int cantidad,
            String motivo
    ) {
        MovimientoInventario movimiento = new MovimientoInventario();

        movimiento.setTipo("SALIDA");
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(LocalDate.now());
        movimiento.setProducto(lote.getProducto());
        movimiento.setLote(lote);
        movimiento.setMotivo(motivo);

        movimientoRepository.save(movimiento);
    }
}
