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


    private Long nodeId;

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "details is required")
    private List<NoteDetailDTO> details;

    private LocalDateTime createdAt;
} 
