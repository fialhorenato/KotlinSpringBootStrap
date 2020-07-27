package com.kotlin.bootstrap.security.service

import com.kotlin.bootstrap.security.dto.UserRequestDTO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository,
        private val encoder: PasswordEncoder
) {
    fun create(userRequestDTO : UserRequestDTO): User {
        val user = User(id = null,
                username = userRequestDTO.username,
                password = encoder.encode(userRequestDTO.password),
                email = userRequestDTO.email,
                roles = userRequestDTO.roles.map { toRole(it) } as MutableList<Role>
        )
        return userRepository.save(user)
    }

    private fun toRole(role: String): Role {
        return Role(role = role, user = null)
    }
}