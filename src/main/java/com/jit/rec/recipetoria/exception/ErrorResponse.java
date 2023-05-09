package com.jit.rec.recipetoria.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime localDateTime,
        String path,
        int statusCode,
        String message,
        String developerMessage
) {
}