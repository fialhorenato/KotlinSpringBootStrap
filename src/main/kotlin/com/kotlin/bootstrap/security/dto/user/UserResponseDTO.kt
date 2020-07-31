package com.kotlin.bootstrap.security.dto.user

import com.kotlin.bootstrap.security.entity.User

data class UserResponseDTO(
        val username : String,
        val email : String,
        val roles : List<String>
) {
    constructor(user : User) : this(username = user.username, email = user.email, roles = user.roles.map { it.role })
}
