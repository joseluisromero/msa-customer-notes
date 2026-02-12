package com.customer.note.exception;

import com.customer.note.dto.error.ProblemDetail;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class CustomServiceException extends RuntimeException {


    private ProblemDetail problemDetail;
    private HttpStatus httpStatus;

    public CustomServiceException(ProblemDetail problemDetail, HttpStatus httpStatus) {
        this.problemDetail = problemDetail;
        this.httpStatus = httpStatus;
    }
}
