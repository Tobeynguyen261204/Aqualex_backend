package com.aquaLex.contract.dto;

import java.math.BigDecimal;

    public record CreateContractRequest(
        String exporterName,
        String importerName,
        String productName,
        BigDecimal quantity,
        String unit,
        String currency,
        BigDecimal unitPrice,
        String incoterm,
        String departurePort,
        String arrivalPort
    ) {}

