package com.example.cinaBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String telefono;
    private String email;
}
