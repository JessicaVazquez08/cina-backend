
package com.example.cinaBackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;

@Service
public class ImagenProductoService {

    @Value("${app.upload.dir:uploads}")
    private String directorio;

    private static final long TAMANO_MAXIMO = 3 * 1024 * 1024;

    private static final Map<String, String> TIPOS_PERMITIDOS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    public String guardar(MultipartFile imagen) {

        if (imagen == null || imagen.isEmpty()) {
            return null;
        }

        // Validar tamaño
        if (imagen.getSize() > TAMANO_MAXIMO) {
            throw new RuntimeException(
                    "La imagen no debe superar los 3 MB"
            );
        }

        // Validar tipo MIME
        String tipo = imagen.getContentType();
        String extension = TIPOS_PERMITIDOS.get(tipo);

        if (extension == null) {
            throw new RuntimeException(
                    "Formato no permitido. Utiliza JPG, PNG o WEBP"
            );
        }

        try {
            // Leer contenido del archivo
            byte[] contenido = imagen.getBytes();

            // Comprobar que la firma corresponda al formato
            if (!esImagenValida(contenido, tipo)) {
                throw new RuntimeException(
                        "El archivo seleccionado no es una imagen válida"
                );
            }

            // Preparar directorio
            Path carpeta = Paths.get(directorio)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(carpeta);

            // Generar nombre único
            String nombreArchivo = UUID.randomUUID() + extension;

            Path destino = carpeta.resolve(nombreArchivo).normalize();

            if (!destino.startsWith(carpeta)) {
                throw new RuntimeException(
                        "La ruta del archivo no es válida"
                );
            }

            // Guardar imagen sin sobrescribir archivos
            Files.write(
                    destino,
                    contenido,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE
            );

            return "/uploads/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo guardar la imagen del producto",
                    e
            );
        }
    }

    // =====================================
    // VALIDAR FIRMA REAL DE LA IMAGEN
    // =====================================
    private boolean esImagenValida(byte[] archivo, String tipo) {

        if (archivo == null || archivo.length < 12) {
            return false;
        }

        // JPEG
        if ("image/jpeg".equals(tipo)) {
            return (archivo[0] & 0xFF) == 0xFF
                    && (archivo[1] & 0xFF) == 0xD8
                    && (archivo[2] & 0xFF) == 0xFF;
        }

        // PNG
        if ("image/png".equals(tipo)) {
            byte[] firma = {
                    (byte) 0x89, 0x50, 0x4E, 0x47,
                    0x0D, 0x0A, 0x1A, 0x0A
            };

            for (int i = 0; i < firma.length; i++) {
                if (archivo[i] != firma[i]) {
                    return false;
                }
            }

            return true;
        }

        // WEBP
        if ("image/webp".equals(tipo)) {
            return archivo[0] == 'R'
                    && archivo[1] == 'I'
                    && archivo[2] == 'F'
                    && archivo[3] == 'F'
                    && archivo[8] == 'W'
                    && archivo[9] == 'E'
                    && archivo[10] == 'B'
                    && archivo[11] == 'P';
        }

        return false;
    }
}
