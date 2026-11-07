package com.aquaLex.auth.dto;

import java.time.Instant;
import java.util.UUID;

import com.aquaLex.auth.domain.UserRole;

public record UserResponse(    
        UUID id,
        String email,
        String userName,
        String fullName,
        UserRole role,
        Instant createdAt,
        Instant updatedAt) {
}
