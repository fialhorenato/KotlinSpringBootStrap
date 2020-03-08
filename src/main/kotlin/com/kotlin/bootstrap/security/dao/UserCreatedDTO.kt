package com.kotlin.bootstrap.security.dao

data class UserCreatedDTO(
        val username : String,
        val password : String,
        val roles : List<String>
)