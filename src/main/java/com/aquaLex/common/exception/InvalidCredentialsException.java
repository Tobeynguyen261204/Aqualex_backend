package com.aquaLex.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super(
                "INVALID_CREDENTIALS",
                "Invalid email or password",
                HttpStatus.UNAUTHORIZED
        );
    }
     public InvalidCredentialsException(String message) {
        super(
            "INVALID_CREDENTIALS",
            message,
            HttpStatus.UNAUTHORIZED
        );
    }
}
