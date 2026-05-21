package com.example.cinaBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cina_venta_detalles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    private Double precioVenta;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    private Lote lote;

    @ManyToOne
    private Venta venta;
}