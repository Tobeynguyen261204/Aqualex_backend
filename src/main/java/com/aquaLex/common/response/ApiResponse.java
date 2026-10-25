package com.aquaLex.common.response;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        Instant timestamp,
        String path
) {
    public static <T> ApiResponse<T> success(
            String code,
            String message,
            T data,
            String path
    ) {
        return new ApiResponse<>(
                true,
                code,
                message,
                data,
                Instant.now(),
                path
        );
    }

    public static <T> ApiResponse<T> failure(
            String code,
            String message,
            String path
    ) {
        return new ApiResponse<>(
                false,
                code,
                message,
                null,
                Instant.now(),
                path
        );
    }
}