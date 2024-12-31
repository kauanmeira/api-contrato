package com.kauanmeira.api_contrato.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                description = "Documentação Teste Desenvolvimento - Attus",
                title = "Documentação Teste Desenvolvimento - Attus",
                version = "1.0",
                license = @License(
                        name = "License name"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        }

)

public class OpenApiConfig {
}