package com.customer.note.dto.error;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProblemDetail {
    String title;
    String detail;
    List<DetailError> errors;
    Date timestamp;
    String message;
    String instance;
    String type;
    String resource;
    String component;
}
