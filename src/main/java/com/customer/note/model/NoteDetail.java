package com.customer.note.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDetail {

    @Column(name = "detail_key", nullable = false)
    private String key;

    @Column(name = "detail_value", nullable = false)
    private String value;

    @Column(name = "sensitive", nullable = false)
    private Boolean sensitive;
}
