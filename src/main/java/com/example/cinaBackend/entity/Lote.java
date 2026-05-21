package com.example.cinaBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "cina_lotes")
@Data
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroLote;
    private LocalDate fechaCaducidad;
    private LocalDate fechaEntrada;

    private Integer stockInicial;
    private Integer stockActual;

    private Double precioCompra;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}