package com.customer.note.exception.handler;

import com.customer.note.dto.error.DetailError;
import com.customer.note.dto.error.ProblemDetail;
import com.customer.note.exception.CustomServiceException;
import com.customer.note.exception.ResourceNotFoundException;
import com.customer.note.model.enums.CodeValidationEnum;
import com.customer.note.util.ConverterErrorUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<DetailError>detailErrors=new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                {
                    CodeValidationEnum codeEnum = CodeValidationEnum.getCode(fe.getDefaultMessage());
                    detailErrors.add(DetailError.builder()
                            .code(codeEnum.getCode())
                            .message(codeEnum.getMessage())
                            .businessMessage(codeEnum.getBusinessMessage())
                            .build());
                }

        );
        ProblemDetail problemDetail= ConverterErrorUtil.getProblemDetail(
                "Validation Error",
                "One or more fields are invalid",
                "Validation failed for the request body",
                null,
                null,
                null,
                request.getRequestURI(),
                detailErrors
        );
        return new ResponseEntity<>(problemDetail,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomServiceException.class)
    public ResponseEntity<Object> customServiceException(CustomServiceException ex) {
        ProblemDetail problemDetail = ex.getProblemDetail();
        HttpStatus status = ex.getHttpStatus();
        return new ResponseEntity<>(problemDetail, status);
    }
}
