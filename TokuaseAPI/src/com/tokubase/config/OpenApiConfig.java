package com.tokubase.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI / Swagger UI configuration.
 * Access the UI at http://localhost:8080/swagger-ui.html
 * Raw spec at     http://localhost:8080/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI tokubaseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TokuBase API")
                        .description("""
                                REST API for the TokuBase Tokusatsu metadata database.
                                Covers **Kamen Rider** (RIDER) and **Super Sentai** (SENTAI) series,
                                their characters, transformation forms, and episode listings.
                                
                                All list endpoints support Spring Data pagination via
                                `?page=0&size=20&sort=name,asc` query parameters.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TokuBase Team")
                                .url("https://github.com/your-org/tokubase")
                                .email("contact@tokubase.io"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local development server")
                ));
    }
}

