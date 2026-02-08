package com.example.cinaBackend.service;

import org.springframework.stereotype.Service;
import com.example.cinaBackend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String email, String password) {
        return userRepository
                .findByEmail(email)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}