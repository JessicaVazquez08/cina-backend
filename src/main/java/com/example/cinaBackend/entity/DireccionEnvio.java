
package com.example.cinaBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cina_direcciones_envio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreDestinatario;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false, length = 20)
    private String numeroExterior;

    private String numeroInterior;

    @Column(nullable = false)
    private String colonia;

    @Column(nullable = false, length = 10)
    private String codigoPostal;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String estado;

    @Column(length = 500)
    private String referencias;

    @Builder.Default
    @Column(nullable = false)
    private Boolean predeterminada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User usuario;
}
