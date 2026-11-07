package com.aquaLex.common.exception;

public class GenerationRequestNotFoundException extends BusinessException {
    public GenerationRequestNotFoundException(String message) {
        super("GENERATION_REQUEST_NOT_FOUND", message, org.springframework.http.HttpStatus.NOT_FOUND);
    }
    
}
