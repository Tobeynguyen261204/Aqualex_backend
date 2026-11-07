package com.aquaLex.ai.contract.application;

import com.aquaLex.ai.contract.domain.GenerationRequestStatus;

public record ContractAnalysisResult(
        String extractedData,
        String missingFields,
        String warnings,
        GenerationRequestStatus status
) {
}
