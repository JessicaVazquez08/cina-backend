package com.example.cinaBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cinaBackend.dto.LoginRequest;
import com.example.cinaBackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        boolean valid = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        if (!valid) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        return ResponseEntity.ok("Login exitoso");
    }
}