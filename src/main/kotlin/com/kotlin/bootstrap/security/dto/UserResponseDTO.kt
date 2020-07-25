package com.kotlin.bootstrap.security.dto

data class UserResponseDTO(
        val username : String,
        val email : String,
        val roles : List<String>
)
