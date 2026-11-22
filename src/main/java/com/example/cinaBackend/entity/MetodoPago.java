
package com.example.cinaBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cina_metodos_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false, length = 20)
    private String tipoTarjeta;

    @Column(nullable = false, length = 4)
    private String ultimosCuatro;

    @Column(nullable = false)
    private Integer mesVencimiento;

    @Column(nullable = false)
    private Integer anioVencimiento;

    @Builder.Default
    @Column(nullable = false)
    private Boolean predeterminado = false;

    // Identificador interno para la simulación.
    // No representa un número de tarjeta real.
    @Column(nullable = false, unique = true)
    private String referenciaSimulada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User usuario;
}
