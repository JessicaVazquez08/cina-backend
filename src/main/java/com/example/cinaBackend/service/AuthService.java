package com.example.cinaBackend.service;

import org.springframework.stereotype.Service;

import com.example.cinaBackend.entity.User;
import com.example.cinaBackend.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String email, String password) {

        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }
}