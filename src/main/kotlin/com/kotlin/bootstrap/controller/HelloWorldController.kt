package com.kotlin.bootstrap.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun helloWorld(): String {
        return "Hello World"
    }
}