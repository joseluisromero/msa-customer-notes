package com.customer.note.helper;

import com.customer.note.dto.traceability.TraceabilityDTO;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Data
public class TraceabilityHelper {

    // Este objeto vivirá solo lo que dure la petición HTTP
    private TraceabilityDTO traceability;

}