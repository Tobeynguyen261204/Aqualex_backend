package com.aquaLex.ai.contract.dto;

import java.time.Instant;
import java.util.UUID;

import com.aquaLex.ai.contract.domain.GenerationRequestStatus;

public record GenerationRequestResponse(
        UUID requestId,
        GenerationRequestStatus status,
        String description,
        Instant createdAt,
        Instant updatedAt,
        String extractedData,
        String missingFields,
        String warnings,
        String draftContent,
        String draftLanguage,
        Integer draftVersion,
        String reviewResult,
        Instant reviewAt
) {
}
