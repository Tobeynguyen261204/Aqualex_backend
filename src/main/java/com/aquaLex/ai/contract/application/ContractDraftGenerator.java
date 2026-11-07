package com.aquaLex.ai.contract.application;

public interface ContractDraftGenerator {
    ContractDraftResult generateDraft(
        String description,
        String extractedData
    );
}
