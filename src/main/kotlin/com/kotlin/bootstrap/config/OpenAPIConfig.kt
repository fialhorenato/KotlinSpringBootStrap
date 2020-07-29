package com.kotlin.bootstrap.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.oas.annotations.EnableOpenApi

@Configuration
@EnableOpenApi
class OpenAPIConfig {

    companion object {
        private const val TITLE = "Kotlin Bootstrap API"
        private const val DESCRIPTION = "This is a Bootstrap application with security features for a project using Kotlin and Spring Boot"
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
                .components(Components())
                .info(Info().title(TITLE).description(DESCRIPTION))
    }
}