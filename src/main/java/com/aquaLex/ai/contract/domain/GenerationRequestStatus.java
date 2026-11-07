package com.aquaLex.ai.contract.domain;

public enum GenerationRequestStatus {
    ANALYZING,
    NEEDS_CONFIRMATION,
    GENERATING,
    COMPLETED,
    FAILED,
    CANCELLED,
    FINALIZED
}
