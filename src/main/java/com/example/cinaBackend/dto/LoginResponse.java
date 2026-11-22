package com.example.cinaBackend.dto;

import com.example.cinaBackend.enums.Rol;

public record LoginResponse(
        Long id,
        String email,
        Rol role,
        String token
) {}