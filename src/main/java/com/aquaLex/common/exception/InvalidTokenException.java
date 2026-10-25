package com.aquaLex.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException(String message) {
        super(
                "INVALID_TOKEN",
                message,
                HttpStatus.UNAUTHORIZED
        );
    }
}
