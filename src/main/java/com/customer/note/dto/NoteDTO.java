package com.customer.note.dto;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDTO {


    private Long noteId;

    @NotBlank(message = "002")
    private String title;

    @NotNull(message = "003")
    private List<NoteDetailDTO> details;

    private LocalDateTime createdAt;
} 
