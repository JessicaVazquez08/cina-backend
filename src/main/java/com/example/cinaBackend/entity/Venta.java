
package com.example.cinaBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cina_ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private Double total;

    // Usuario que realizó la compra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User usuario;

    // Datos históricos de envío
    private String nombreDestinatario;
    private String telefonoEnvio;
    private String calleEnvio;
    private String numeroExteriorEnvio;
    private String numeroInteriorEnvio;
    private String coloniaEnvio;
    private String codigoPostalEnvio;
    private String ciudadEnvio;
    private String estadoEnvio;

    @Column(length = 500)
    private String referenciasEnvio;

    // Datos históricos del método de pago simulado
    private String tipoTarjeta;
    private String ultimosCuatroTarjeta;

    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<VentaDetalle> detalles;
}
