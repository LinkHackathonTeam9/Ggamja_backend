package com.ggamja.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Ggamja API",
                version = "v1",
                description = "깜자 API 명세서"
        )
)
@Configuration
public class SwaggerConfig {
}
