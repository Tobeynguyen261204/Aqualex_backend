package com.aquaLex.contract.dto;

import java.math.BigDecimal;

public record UpdateContractRequest(
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
){}

