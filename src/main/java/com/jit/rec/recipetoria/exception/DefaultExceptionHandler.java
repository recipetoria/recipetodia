package com.jit.rec.recipetoria.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UsernameNotFoundException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(EmailAlreadyExistsException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();

        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                String.join(", ", errorMessages),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String rootCause = e.getMostSpecificCause().getMessage();
        String errorMessage;
        String variableName = null;
        String variableTypeShort = null;
        String variableTypeFull = null;

        Matcher matcher1 = Pattern.compile("Cannot deserialize value of type `(.+?)`").matcher(rootCause);
        if (matcher1.find()) {
            variableTypeFull = matcher1.group(1);
            String[] parts = variableTypeFull.split("\\.");
            variableTypeShort = parts[parts.length - 1];
        }

        Matcher matcher2 = Pattern.compile("through reference chain: .+?\\[\"(.+?)\"]").matcher(rootCause);
        if (matcher2.find()) {
            variableName = matcher2.group(1);
        }

        errorMessage = "Failed to process request. Invalid value for variable '" + variableName + "' of type '" +
                variableTypeShort + "'.";

        try {
            Class<?> enumClass = Class.forName(variableTypeFull);
            if (enumClass.isEnum()) {
                @SuppressWarnings("unchecked")
                Enum<?>[] enumValues = ((Class<Enum<?>>) enumClass).getEnumConstants();
                String allowedValues = Arrays.stream(enumValues)
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));
                errorMessage += String.format(" Allowed values are: %s", allowedValues);
            }
        } catch (ClassNotFoundException ignored) {
        }

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String expectedType = Objects.requireNonNull(e.getRequiredType()).getSimpleName();
        String actualValue = Objects.requireNonNull(e.getValue()).toString();

        String errorMessage = "Failed to convert value '" + actualValue + "' to required type '" + expectedType + "'";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleException(MaxUploadSizeExceededException e, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleException(IOException e, HttpServletRequest request) {

        ErrorResponse apiError = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
