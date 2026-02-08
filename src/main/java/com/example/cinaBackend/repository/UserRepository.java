package com.example.cinaBackend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cinaBackend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
