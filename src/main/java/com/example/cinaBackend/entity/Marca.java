package com.example.cinaBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cina_marcas")
@Data
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
}