package com.aquaLex.contract.dto;

import java.time.Instant;
import java.util.UUID;

import com.aquaLex.contract.domain.UserRole;

public record UserResponse(    
        UUID id,
        String email,
        String userName,
        String fullName,
        UserRole role,
        Instant createdAt,
        Instant updatedAt) {
}
