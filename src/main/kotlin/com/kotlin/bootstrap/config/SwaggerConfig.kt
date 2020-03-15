package com.kotlin.bootstrap.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .securitySchemes(securitySchemes())
            .securityContexts(securityContexts())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.any())
            .build()


    fun securitySchemes() = listOf(ApiKey("JWT", AUTHORIZATION, "header"))

    fun securityContexts(): MutableList<SecurityContext>? =
            mutableListOf(SecurityContext
                    .builder()
                    .securityReferences(
                            listOf(SecurityReference
                                    .builder()
                                    .reference("JWT")
                                    .scopes(
                                    arrayOf(AuthorizationScope("global", "accessEverything")))
                                    .build()
                            )
                    )
                    .build())
}