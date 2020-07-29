package com.kotlin.bootstrap.security.service

import com.kotlin.bootstrap.security.dto.UserRequestDTO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.repository.UserRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.OAuth2Authentication
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
                roles = listOf(Role(null,null, "USER"))
        )
        return userRepository.save(user)
    }

    fun me(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        if(authentication.principal is String) {
            val username = authentication.principal as String
            if (username.equals("anonymousUser")) {
                throw AccessDeniedException("User not authenticated")
            }
            return userRepository.findByUsername(username) ?: throw NotFoundException("User could not be found")
        } else {
            throw AccessDeniedException("User is anonymous or is not identified")
        }
    }
}