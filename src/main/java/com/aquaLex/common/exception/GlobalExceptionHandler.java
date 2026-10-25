package com.aquaLex.common.exception;

import com.aquaLex.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {
        ApiResponse<Void> response = ApiResponse.failure(
                exception.getCode(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(exception.getStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        ApiResponse<Void> response = ApiResponse.failure(
                "INVALID_REQUEST",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .findFirst()
                .orElse("Invalid request");

        ApiResponse<Void> response = ApiResponse.failure(
                "VALIDATION_FAILED",
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableMessage(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        String message = "Request body is invalid";
        Throwable cause = exception.getCause();

        if (cause instanceof IllegalArgumentException illegalArgumentException
                && illegalArgumentException.getMessage() != null) {
            message = illegalArgumentException.getMessage();
        }

        ApiResponse<Void> response = ApiResponse.failure(
                "INVALID_REQUEST",
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        Throwable rootCause = getRootCause(exception);

        log.error(
                "Unexpected error | method={} | path={} | exception={} | rootCause={} | message={}",
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getName(),
                rootCause.getClass().getName(),
                rootCause.getMessage(),
                exception
        );

        ApiResponse<Void> response = ApiResponse.failure(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private Throwable getRootCause(Throwable exception) {
        Throwable rootCause = exception;

        while (rootCause.getCause() != null
                && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }

        return rootCause;
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
