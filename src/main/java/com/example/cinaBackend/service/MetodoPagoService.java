
package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.MetodoPago;
import com.example.cinaBackend.entity.User;
import com.example.cinaBackend.repository.MetodoPagoRepository;
import com.example.cinaBackend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class MetodoPagoService {

    private final MetodoPagoRepository metodoRepository;
    private final UserRepository userRepository;

    public MetodoPagoService(
            MetodoPagoRepository metodoRepository,
            UserRepository userRepository
    ) {
        this.metodoRepository = metodoRepository;
        this.userRepository = userRepository;
    }

    public List<MetodoPago> listar(String email) {
        User usuario = obtenerUsuario(email);

        return metodoRepository.findByUsuarioId(usuario.getId());
    }

    @Transactional
    public MetodoPago crear(MetodoPago datos, String email) {
        User usuario = obtenerUsuario(email);

        validarDatos(datos);

        datos.setId(null);
        datos.setUsuario(usuario);

        // La referencia se genera exclusivamente en el backend.
        datos.setReferenciaSimulada(UUID.randomUUID().toString());

        if (Boolean.TRUE.equals(datos.getPredeterminado())) {
            quitarPredeterminados(usuario.getId());
        }

        return metodoRepository.save(datos);
    }

    @Transactional
    public MetodoPago actualizar(
            Long id,
            MetodoPago datos,
            String email
    ) {
        User usuario = obtenerUsuario(email);

        MetodoPago metodo = buscarMetodo(id, usuario.getId());

        validarDatos(datos);

        metodo.setTitular(datos.getTitular());
        metodo.setTipoTarjeta(datos.getTipoTarjeta());
        metodo.setUltimosCuatro(datos.getUltimosCuatro());
        metodo.setMesVencimiento(datos.getMesVencimiento());
        metodo.setAnioVencimiento(datos.getAnioVencimiento());

        if (Boolean.TRUE.equals(datos.getPredeterminado())) {
            quitarPredeterminados(usuario.getId());
            metodo.setPredeterminado(true);
        }

        return metodoRepository.save(metodo);
    }

    @Transactional
    public void eliminar(Long id, String email) {
        User usuario = obtenerUsuario(email);

        MetodoPago metodo = buscarMetodo(id, usuario.getId());

        metodoRepository.delete(metodo);
    }

    @Transactional
    public MetodoPago marcarPredeterminado(
            Long id,
            String email
    ) {
        User usuario = obtenerUsuario(email);

        MetodoPago metodo = buscarMetodo(id, usuario.getId());

        quitarPredeterminados(usuario.getId());

        metodo.setPredeterminado(true);

        return metodoRepository.save(metodo);
    }

    private void validarDatos(MetodoPago datos) {

        if (datos.getTitular() == null ||
                datos.getTitular().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del titular es obligatorio"
            );
        }

        if (datos.getTipoTarjeta() == null ||
                !(datos.getTipoTarjeta().equals("CREDITO") ||
                        datos.getTipoTarjeta().equals("DEBITO"))) {
            throw new IllegalArgumentException(
                    "El tipo de tarjeta debe ser CREDITO o DEBITO"
            );
        }

        if (datos.getUltimosCuatro() == null ||
                !datos.getUltimosCuatro().matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "Se requieren exactamente los últimos cuatro dígitos"
            );
        }

        if (datos.getMesVencimiento() == null ||
                datos.getAnioVencimiento() == null ||
                datos.getMesVencimiento() < 1 ||
                datos.getMesVencimiento() > 12) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento no es válida"
            );
        }

        YearMonth vencimiento = YearMonth.of(
                datos.getAnioVencimiento(),
                datos.getMesVencimiento()
        );

        if (vencimiento.isBefore(YearMonth.now())) {
            throw new IllegalArgumentException(
                    "La tarjeta está vencida"
            );
        }
    }

    private User obtenerUsuario(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );
    }

    private MetodoPago buscarMetodo(Long id, Long usuarioId) {
        return metodoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Método de pago no encontrado"
                        )
                );
    }

    private void quitarPredeterminados(Long usuarioId) {
        List<MetodoPago> metodos =
                metodoRepository.findByUsuarioId(usuarioId);

        metodos.forEach(metodo ->
                metodo.setPredeterminado(false)
        );

        metodoRepository.saveAll(metodos);
    }
}
