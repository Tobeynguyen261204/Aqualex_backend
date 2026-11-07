package com.aquaLex.ai.contract.application;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.aquaLex.ai.contract.dto.ContractReviewResult;

@Service
public class MockContractReviewService implements ContractReviewService {

    @Override
    public ContractReviewResult review(
            String draftContent,
            String extractedData
    ) {
        String review = """
                {
                  "overallResult": "NEEDS_REVIEW",
                  "warnings": [
                    {
                      "code": "MISSING_PARTY_ADDRESS",
                      "severity": "HIGH",
                      "message": "Seller and buyer addresses should be confirmed."
                    },
                    {
                      "code": "MISSING_DELIVERY_DATE",
                      "severity": "MEDIUM",
                      "message": "The delivery date is not specified."
                    },
                    {
                      "code": "QUALITY_STANDARD_NOT_SPECIFIED",
                      "severity": "MEDIUM",
                      "message": "Product quality and inspection standards should be specified."
                    }
                  ],
                  "disclaimer": "This review is an AI-generated checklist and is not legal advice."
                }
                """.trim();

        return new ContractReviewResult(review, Instant.now());
    }
}
