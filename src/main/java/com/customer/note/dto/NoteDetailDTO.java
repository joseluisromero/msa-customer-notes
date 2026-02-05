package com.customer.note.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDetailDTO {
    private String key;
    private String value;
    private Boolean sensitive;
}
