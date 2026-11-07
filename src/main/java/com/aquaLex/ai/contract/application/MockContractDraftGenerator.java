package com.aquaLex.ai.contract.application;

import org.springframework.stereotype.Service;

@Service
public class MockContractDraftGenerator implements ContractDraftGenerator {

    @Override
    public ContractDraftResult generateDraft(
            String description,
            String extractedData
    ) {
        String draftContent = """
                FROZEN SEAFOOD SALES CONTRACT

                1. PARTIES

                Seller: ABC Seafood, Vietnam
                Buyer: German Food GmbH, Germany

                2. PRODUCTS

                The Seller agrees to sell frozen seafood products
                according to the specifications confirmed by both parties.

                3. PRICE AND QUANTITY

                Quantity and price shall be based on the confirmed
                contract information.

                4. DELIVERY

                Delivery shall be made according to the agreed
                Incoterms and shipment schedule.

                5. PAYMENT

                Payment shall be made according to the payment terms
                confirmed by the Buyer and the Seller.

                6. GOVERNING LAW

                This contract shall be governed by the applicable law
                agreed by both parties.

                7. SIGNATURES

                SELLER: ____________________

                BUYER: ____________________
                """.trim();

        return new ContractDraftResult(
                draftContent,
                "en",
                1
        );
    }
    
}
