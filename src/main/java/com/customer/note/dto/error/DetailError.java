package com.customer.note.dto.error;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailError {
    String code;
    String message;
    String businessMessage;
}
