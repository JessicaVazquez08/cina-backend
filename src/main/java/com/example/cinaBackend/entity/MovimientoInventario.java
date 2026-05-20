package com.example.cinaBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "movimientos")
@Data
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // ENTRADA / SALIDA
    private Integer cantidad;
    private LocalDate fecha;
    private String motivo;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    private Lote lote;
}