package com.customer.note.dto.traceability;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraceabilityDTO {

    @NotBlank(message = "La cabecera x-guid es obligatoria")
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "El formato de x-guid es inválido (debe ser UUID)"
    )
    private String xGuid;
    @NotBlank(message = "La cabecera x-user es obligatoria")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    private String xUser;
    @NotBlank(message = "La cabecera x-device es obligatoria")
    private String xDevice;
    @Pattern(
            regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$",
            message = "El formato de x-device-ip no es válido"
    )
    private String xDeviceIp;

    private String xSession;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestDate;

}