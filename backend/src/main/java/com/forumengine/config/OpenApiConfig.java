package com.forumengine.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ForumEngine RestAPI",
                version = "1.0",
                description = "Default admin account: username: admin, password: admin",
                contact = @Contact(
                        name = "Jakub Chojdak",
                        email = "jchojdak@gmail.com",
                        url = "https://github.com/jchojdak"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "JWT Bearer token used for authentication",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
