package com.aquaLex.ai.contract.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateDraftRequest(
        @NotBlank(message = "draftContent must not be blank")
        String draftContent
) {
}
