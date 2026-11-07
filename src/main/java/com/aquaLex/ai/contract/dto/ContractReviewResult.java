package com.aquaLex.ai.contract.dto;

import java.time.Instant;

public record ContractReviewResult(
    String reviewResult,
    Instant reviewAt
) {
    
}
