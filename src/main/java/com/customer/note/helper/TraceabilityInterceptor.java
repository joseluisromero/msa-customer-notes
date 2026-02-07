package com.customer.note.helper;

import com.customer.note.dto.error.DetailError;
import com.customer.note.dto.error.ProblemDetail;
import com.customer.note.dto.traceability.TraceabilityDTO;
import com.customer.note.exception.CustomServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TraceabilityInterceptor implements HandlerInterceptor {

    private static final Map<String, String> HEADER_MAPPING = Map.of(
            "xUser", "x-user",
            "xGuid", "x-guid",
            "xDevice", "x-device",
            "xDeviceIp", "x-device-ip",
            "xSession", "x-session"
    );
    private final TraceabilityHelper traceabilityHelper;
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
        List<DetailError> details = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();

            // Buscamos el nombre real, si no existe devolvemos el original
            String headerName = HEADER_MAPPING.getOrDefault(fieldName, fieldName);
            details.add(DetailError.builder()
                    .code("001")
                    .message(headerName)
                    .businessMessage(violation.getMessage())
                    .build());
        }
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Invalid Request Headers")
                .detail("One or more required headers are missing or invalid")
                .errors(details)
                .timestamp(LocalDateTime.now())
                .message("Validation error in request headers")
                .instance("/api/notes") // Ejemplo de endpoint
                .component("TraceabilityInterceptor")
                .resource("TraceabilityDTO")
                .type(request.getRequestURI())
                .build();
        // 3. Si hay errores, lanzamos la excepción y detenemos el flujo
        if (!violations.isEmpty()) {
            // Casteo seguro o uso de wildcard en la excepción
            throw new CustomServiceException(problemDetail, HttpStatus.BAD_REQUEST);
        }

        // 4. Si todo está bien, guardamos en el contexto
        dto.setRequestDate(LocalDateTime.now()); // Aseguramos que la fecha de la petición sea la actual
        traceabilityHelper.setTraceability(dto);

        return true;
    }
}
