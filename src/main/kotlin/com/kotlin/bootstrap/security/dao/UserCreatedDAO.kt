package com.kotlin.bootstrap.security.dao

data class UserCreatedDAO(
        val username : String,
        val password : String,
        val roles : List<String>
)