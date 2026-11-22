
package com.example.cinaBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String directorio;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path rutaDirectorio = Paths.get(directorio)
                .toAbsolutePath()
                .normalize();

        String ruta = rutaDirectorio.toUri().toString();

        // Asegurar que la ruta termine en /
        if (!ruta.endsWith("/")) {
            ruta += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(ruta);
    }
}
