package com.example.cinaBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cina_productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Integer stock;
    private Integer stockMinimo;
    private Double precioActual;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    public void setPrecioActual(Double precioActual) {
        this.precioActual = precioActual;
    }
}