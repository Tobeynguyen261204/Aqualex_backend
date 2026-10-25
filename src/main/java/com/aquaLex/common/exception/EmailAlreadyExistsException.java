package com.aquaLex.common.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException() {
        super(
                "EMAIL_ALREADY_EXISTS",
                "An account with this email already exists",
                HttpStatus.CONFLICT
        );
    }
}
