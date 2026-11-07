package com.aquaLex.ai.contract.application;

public record ContractDraftResult(
    String content,
    String language,
    Integer version
) {
    
}
