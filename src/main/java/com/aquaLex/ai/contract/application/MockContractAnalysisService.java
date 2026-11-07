package com.aquaLex.ai.contract.application;

import org.springframework.stereotype.Service;

import com.aquaLex.ai.contract.domain.GenerationRequestStatus;

@Service
public class MockContractAnalysisService implements ContractAnalysisService {

    @Override
    public ContractAnalysisResult analyze(String description) {
        return new ContractAnalysisResult(
                """
                {
                  "seller": "ABC Seafood",
                  "buyer": "German Food GmbH",
                  "product": "Frozen shrimp",
                  "quantity": "20 tons",
                  "price": "8 USD/kg",
                  "deliveryTerm": "CIF Hamburg",
                  "paymentTerm": "L/C"
                }
                """.trim(),
                """
                [
                  "sellerAddress",
                  "buyerAddress",
                  "deliveryDate"
                ]
                """.trim(),
                """
                [
                  "Payment condition needs confirmation",
                  "Product quality standard is not specified"
                ]
                """.trim(),
                GenerationRequestStatus.NEEDS_CONFIRMATION
        );
    }
}
