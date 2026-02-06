package com.customer.note.helper;

import com.customer.note.dto.traceability.TraceabilityDTO;
import com.customer.note.exception.HeaderValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TraceabilityInterceptor implements HandlerInterceptor {


    private final TraceabilityHolder traceabilityHolder;
    private final Validator validator; // Inyectamos el validador estándar de Spring

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 1. Construir el DTO
        TraceabilityDTO dto = TraceabilityDTO.builder()
                .xGuid(request.getHeader("x-guid"))
                .xUser(request.getHeader("x-user"))
                .xDevice(request.getHeader("x-device"))
                .xDeviceIp(request.getHeader("x-device-ip"))
                .xSession(request.getHeader("x-session"))
                .build();

        // 2. Ejecutar validación programática
        // El ? wildcard se usa porque validate retorna ConstraintViolation<T>
        Set<ConstraintViolation<TraceabilityDTO>> violations = validator.validate(dto);

        // 3. Si hay errores, lanzamos la excepción y detenemos el flujo
        if (!violations.isEmpty()) {
            // Casteo seguro o uso de wildcard en la excepción
            //throw new HeaderValidationException((Set) violations);
            throw new ConstraintViolationException(violations);
        }

        // 4. Si todo está bien, guardamos en el contexto
        traceabilityHolder.setTraceability(dto);

        return true;
    }
}
