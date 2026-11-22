
package com.example.cinaBackend.dto;

import java.util.List;

public class CompraRequest {

    private Long direccionId;
    private Long metodoPagoId;
    private List<DetalleCompraRequest> detalles;

    public Long getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Long direccionId) {
        this.direccionId = direccionId;
    }

    public Long getMetodoPagoId() {
        return metodoPagoId;
    }

    public void setMetodoPagoId(Long metodoPagoId) {
        this.metodoPagoId = metodoPagoId;
    }

    public List<DetalleCompraRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraRequest> detalles) {
        this.detalles = detalles;
    }
}
