package org.kush.vaultyauth.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerHandlerAdvice
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex)
    {
        var ex2 = ex.getCause();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex2 == null ? ex.getMessage() : ex2.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
