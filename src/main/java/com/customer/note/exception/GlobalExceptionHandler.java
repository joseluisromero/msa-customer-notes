package com.customer.note.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Map<String, String> HEADER_MAPPING = Map.of(
            "xUser", "x-user",
            "xGuid", "x-guid",
            "xDevice", "x-device",
            "xDeviceIp", "x-device-ip",
            "xSession", "x-session"
    );
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> body = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> body.put(fe.getField(), fe.getDefaultMessage()));
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HeaderValidationException.class)
    public ResponseEntity<Map<String, Object>> handleHeaderValidation(HeaderValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid Headers");

        Map<String, String> details = new HashMap<>();
        ex.getViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            details.put(fieldName, errorMessage);
        });

        response.put("details", details);

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(jakarta.validation.ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();

            // Buscamos el nombre real, si no existe devolvemos el original
            String headerName = HEADER_MAPPING.getOrDefault(fieldName, fieldName);

            errors.put(headerName, violation.getMessage());
        }

        // ... resto del código igual
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Header Validation Failed");
        response.put("details", errors);
        return ResponseEntity.badRequest().body(response);
    }
}
