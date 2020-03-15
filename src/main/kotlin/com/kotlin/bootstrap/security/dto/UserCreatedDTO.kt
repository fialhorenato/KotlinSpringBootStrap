package com.kotlin.bootstrap.security.dto

data class UserCreatedDTO(
        val username : String,
        val password : String,
        val roles : List<String>
)