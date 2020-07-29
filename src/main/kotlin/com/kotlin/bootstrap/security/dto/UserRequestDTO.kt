package com.kotlin.bootstrap.security.dto

data class UserRequestDTO(
        val username : String,
        val email : String,
        val password: String
)
