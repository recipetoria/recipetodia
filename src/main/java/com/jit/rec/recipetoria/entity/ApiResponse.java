package com.jit.rec.recipetoria.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@SuperBuilder
@JsonInclude(NON_NULL)
public record ApiResponse(
        LocalDateTime timeStamp,
        int statusCode,
        HttpStatus status,
        String reason,
        String message,
        String developerMessage,
        Map<?, ?> data
) {
}