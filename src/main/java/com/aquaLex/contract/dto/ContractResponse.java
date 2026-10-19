package com.aquaLex.contract.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.aquaLex.contract.domain.ContractStatus;

public record ContractResponse(
        UUID id,
        String exporterName,
        String importerName,
        String productName,
        BigDecimal quantity,
        String unit,
        String currency,
        BigDecimal unitPrice,
        String incoterm,
        String departurePort,
        String arrivalPort,
        ContractStatus status,
        Instant createdAt
) {}