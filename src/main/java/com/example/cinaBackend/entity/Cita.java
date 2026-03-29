package com.example.cinaBackend.entity;

import com.example.cinaBackend.enums.EstadoCita;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}
