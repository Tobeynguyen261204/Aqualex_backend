package com.aquaLex.auth.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aquaLex.auth.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}
    