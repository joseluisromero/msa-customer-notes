package com.customer.note.configuration;

import com.customer.note.helper.TraceabilityInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final TraceabilityInterceptor traceabilityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aplica a todas las rutas
        registry.addInterceptor(traceabilityInterceptor).addPathPatterns("/**");
    }
}
