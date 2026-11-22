package com.example.cinaBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.cinaBackend.dto.LoginRequest;
import com.example.cinaBackend.dto.LoginResponse;
import com.example.cinaBackend.service.AuthService;
import com.example.cinaBackend.service.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            JwtService jwtService) {

        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        var usuario = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        if (usuario.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body("Credenciales inválidas");
        }

        var user = usuario.get();

        String token = jwtService.generateToken(user);

        LoginResponse respuesta = new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token
        );

        return ResponseEntity.ok(respuesta);
    }
}