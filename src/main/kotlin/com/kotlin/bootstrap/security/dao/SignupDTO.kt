package com.kotlin.bootstrap.security.dao

data class SignupDTO(
        val username: String,
        val email: String,
        val password: String,
        val roles: Set<String>
)