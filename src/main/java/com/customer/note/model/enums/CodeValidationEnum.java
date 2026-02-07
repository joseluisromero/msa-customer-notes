package com.customer.note.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum CodeValidationEnum {

    REQUIRED_TITLE_NOT_BLANK("002", "title", "Title must not be blank", HttpStatus.BAD_REQUEST),
    REQUIRED_DETAILS_NOT_NULL("003", "details", "Details must not be null", HttpStatus.BAD_REQUEST),
    VALIDATION_NOTE_ID_NOT_FOUND("004", "noteId", "Note not found with noteId=%s", HttpStatus.NOT_FOUND),
    VALIDATION_NOTE_ID_NOT_EQUAL("005", "noteId", "Note not found with noteId=%s and Id %s not equals", HttpStatus.BAD_REQUEST),


    VALIDATION_CODE_GENERIC("1000", "data", "code not get in enum", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final String businessMessage;
    private final HttpStatus httpStatus;

    CodeValidationEnum(String code, String message, String businessMessage, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.businessMessage = businessMessage;
        this.httpStatus = httpStatus;
    }

    public static CodeValidationEnum getCode(String code) {
        return Arrays.stream(CodeValidationEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
