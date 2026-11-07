package com.aquaLex.ai.contract.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviseDraftRequest(
        @NotBlank(message = "instruction must not be blank")
        String instruction
) {
}
