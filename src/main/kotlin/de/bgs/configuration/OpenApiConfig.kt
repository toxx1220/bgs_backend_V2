package de.bgs.configuration

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig() {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .addServersItem(
                Server()
                    .url("https://bgsearch.toxx.dev")
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Datenschutz")
                    .url("https://toxx.dev/datenschutz"),
            )
            .info(
                Info()
                    .contact(
                        Contact()
                            .name("Impressum")
                            .url("https://toxx.dev/impressum")
                    )
            )
    }
}
