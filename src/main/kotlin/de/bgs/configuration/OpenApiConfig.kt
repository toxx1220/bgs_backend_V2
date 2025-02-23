package de.bgs.configuration

import io.swagger.v3.oas.models.OpenAPI
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
                    .url("https://bgsearch.duckdns.org")
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
            )
    }
}
