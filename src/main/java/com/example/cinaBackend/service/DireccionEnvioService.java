
package com.example.cinaBackend.service;

import com.example.cinaBackend.entity.DireccionEnvio;
import com.example.cinaBackend.entity.User;
import com.example.cinaBackend.repository.DireccionEnvioRepository;
import com.example.cinaBackend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DireccionEnvioService {

    private final DireccionEnvioRepository direccionRepository;
    private final UserRepository userRepository;

    public DireccionEnvioService(
            DireccionEnvioRepository direccionRepository,
            UserRepository userRepository
    ) {
        this.direccionRepository = direccionRepository;
        this.userRepository = userRepository;
    }

    public List<DireccionEnvio> listar(String email) {
        User usuario = obtenerUsuario(email);

        return direccionRepository.findByUsuarioId(usuario.getId());
    }

    @Transactional
    public DireccionEnvio crear(
            DireccionEnvio direccion,
            String email
    ) {
        User usuario = obtenerUsuario(email);

        direccion.setId(null);
        direccion.setUsuario(usuario);

        if (Boolean.TRUE.equals(direccion.getPredeterminada())) {
            quitarPredeterminadas(usuario.getId());
        }

        return direccionRepository.save(direccion);
    }

    @Transactional
    public DireccionEnvio actualizar(
            Long id,
            DireccionEnvio datos,
            String email
    ) {
        User usuario = obtenerUsuario(email);

        DireccionEnvio direccion = buscarDireccion(id, usuario.getId());

        direccion.setNombreDestinatario(datos.getNombreDestinatario());
        direccion.setTelefono(datos.getTelefono());
        direccion.setCalle(datos.getCalle());
        direccion.setNumeroExterior(datos.getNumeroExterior());
        direccion.setNumeroInterior(datos.getNumeroInterior());
        direccion.setColonia(datos.getColonia());
        direccion.setCodigoPostal(datos.getCodigoPostal());
        direccion.setCiudad(datos.getCiudad());
        direccion.setEstado(datos.getEstado());
        direccion.setReferencias(datos.getReferencias());

        if (Boolean.TRUE.equals(datos.getPredeterminada())) {
            quitarPredeterminadas(usuario.getId());
            direccion.setPredeterminada(true);
        }

        return direccionRepository.save(direccion);
    }

    @Transactional
    public void eliminar(Long id, String email) {
        User usuario = obtenerUsuario(email);

        DireccionEnvio direccion = buscarDireccion(id, usuario.getId());

        direccionRepository.delete(direccion);
    }

    @Transactional
    public DireccionEnvio marcarPredeterminada(
            Long id,
            String email
    ) {
        User usuario = obtenerUsuario(email);

        DireccionEnvio direccion = buscarDireccion(id, usuario.getId());

        quitarPredeterminadas(usuario.getId());

        direccion.setPredeterminada(true);

        return direccionRepository.save(direccion);
    }

    private User obtenerUsuario(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );
    }

    private DireccionEnvio buscarDireccion(
            Long id,
            Long usuarioId
    ) {
        return direccionRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Dirección no encontrada"
                        )
                );
    }

    private void quitarPredeterminadas(Long usuarioId) {
        List<DireccionEnvio> direcciones =
                direccionRepository.findByUsuarioId(usuarioId);

        direcciones.forEach(direccion ->
                direccion.setPredeterminada(false)
        );

        direccionRepository.saveAll(direcciones);
    }
}
