package com.kotlin.bootstrap.factory

import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User

class UserFactory {
    companion object {
        fun generateUser(): User {
            return User(1L, "username", "email", "password", listOf(generateRole(null)))
        }

        fun generateRole(user : User?): Role {
            return Role(1L, user, "role")
        }
    }
}