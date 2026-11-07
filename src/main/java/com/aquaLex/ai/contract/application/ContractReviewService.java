package com.aquaLex.ai.contract.application;

import com.aquaLex.ai.contract.dto.ContractReviewResult;

public interface ContractReviewService {

    ContractReviewResult review(String draftContent, String extractedData);
}
