package com.aquaLex.ai.contract.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;

public record ConfirmGenerationRequest(
    @NotNull(message = "confirmed must not be null")
    Boolean isConfirmed,
    Map<String, Object> additionalData
) {
    
}
