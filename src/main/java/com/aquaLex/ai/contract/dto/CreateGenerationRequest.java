package com.aquaLex.ai.contract.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGenerationRequest(
        @NotBlank(message = "description must not be blank")
        String description
) {
}
