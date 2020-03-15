package com.kotlin.bootstrap.security.dto

import com.kotlin.bootstrap.security.entity.User

data class UserCreatedDTO(
        val username : String,
        val password : String,
        val roles : List<String>
) {
    constructor(user : User) : this(user.username, user.password, user.roles.map { it.role }.toList())
}