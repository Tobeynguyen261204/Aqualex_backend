package com.aquaLex.ai.contract.infrastructure;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aquaLex.ai.contract.domain.GenerationRequest;

public interface GenerationRequestRepository extends JpaRepository<GenerationRequest, UUID> {
    Optional<GenerationRequest> findByIdAndUser_Email(
            UUID requestId,
            String email
    );
}
