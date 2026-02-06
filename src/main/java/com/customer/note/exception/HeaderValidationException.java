package com.customer.note.exception;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
public class HeaderValidationException extends RuntimeException {

    private final Set<ConstraintViolation<?>> violations;

    public HeaderValidationException(Set<ConstraintViolation<?>> violations) {
        super("Error de validación en cabeceras de trazabilidad");
        this.violations = violations;
    }
}