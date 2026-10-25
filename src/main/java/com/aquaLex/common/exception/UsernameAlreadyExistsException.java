package com.aquaLex.common.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BusinessException {

    public UsernameAlreadyExistsException() {
        super(
                "USERNAME_ALREADY_EXISTS",
                "An account with this username already exists",
                HttpStatus.CONFLICT
        );
    }
}
