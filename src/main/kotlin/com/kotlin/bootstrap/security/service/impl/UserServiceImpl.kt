package com.kotlin.bootstrap.security.service.impl

import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.repository.UserRepository
import com.kotlin.bootstrap.security.service.UserService
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val encoder: PasswordEncoder
) : UserService {
    override fun findByEmail(email : String) : User {
        return userRepository.findByEmail(email) ?: throw NotFoundException("User could not be found")
    }

    override fun create(username : String, password : String, email : String): User {
        val user = User(id = null,
                username = username,
                password = encoder.encode(password),
                email = email,
                roles = listOf(Role(null,null, "USER"))
        )
        return userRepository.save(user)
    }

    override fun me(): User {
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

    override fun deleteById(id : Long) {
       userRepository.deleteById(id)
    }

    override fun deleteByEmail(email : String) {
        userRepository.deleteByEmail(email)
    }
}