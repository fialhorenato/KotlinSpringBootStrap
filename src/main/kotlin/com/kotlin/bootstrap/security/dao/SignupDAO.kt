package com.kotlin.bootstrap.security.dao

data class SignupDAO(
        val username: String,
        var email: String,
        val password: String,
        var roles: Set<String>
)