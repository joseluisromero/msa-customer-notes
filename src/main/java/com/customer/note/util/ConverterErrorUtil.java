package com.customer.note.util;

import com.customer.note.dto.error.DetailError;
import com.customer.note.dto.error.ProblemDetail;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ConverterErrorUtil {

    public static ProblemDetail getProblemDetail(String title, String detail, String message, String instance, String component, String resource, String type, List<DetailError> details) {

        ProblemDetail problemDetail = ProblemDetail.builder()
                .title(title)
                .detail(detail)
                .errors(details)
                .timestamp(LocalDateTime.now())
                .message(message)
                .instance(instance)
                .component(component)
                .resource(resource)
                .type(type)
                .build();
        return problemDetail;
    }
    public static ProblemDetail getProblemDetail(String message, String component,  List<DetailError> details) {

        ProblemDetail problemDetail = ProblemDetail.builder()
                .title(NotesConstantsUtil.TITLE)
                .detail(NotesConstantsUtil.DETAIL)
                .errors(details)
                .timestamp(LocalDateTime.now())
                .message(message)
                .component(component)
                .build();
        return problemDetail;
    }


    public static DetailError convertToDetailError(String code, String message, String businessMessage) {

        return DetailError.builder()
                .code(code)
                .message(message)
                .businessMessage(businessMessage)
                .build();
    }
}