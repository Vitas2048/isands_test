package ru.isis_test.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Isands test API",
                version = "v1.0",
                description = "RESTFUL API for isands test"
        )
)
public class SwaggerConfig {
}
