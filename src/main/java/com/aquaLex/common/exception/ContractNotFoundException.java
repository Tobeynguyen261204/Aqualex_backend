package com.aquaLex.common.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

public class ContractNotFoundException extends BusinessException {

    public ContractNotFoundException(UUID contractId) {
        super(
                "CONTRACT_NOT_FOUND",
                "Contract not found: " + contractId,
                HttpStatus.NOT_FOUND
        );
    }
}
