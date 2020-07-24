package com.kotlin.bootstrap.security.dto

import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.service.UserDetails


data class UserResponseDTO(
        val username : String,
        val email : String,
        val roles : List<String>
) {
    constructor(userDetails : UserDetails) : this(userDetails.myUsername ,userDetails.email, userDetails.roles)
    constructor(user : User) : this(user.username, user.email, user.roles.map { it.role })
}