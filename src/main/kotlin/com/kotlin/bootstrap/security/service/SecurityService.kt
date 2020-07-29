package com.kotlin.bootstrap.security.service

import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Propagation.SUPPORTS
import org.springframework.transaction.annotation.Transactional

@Service
class SecurityService(private val userRepository: UserRepository) : UserDetailsService {

    companion object {
        const val ROLE_PREFIX = "ROLE_"
    }

    @Transactional(propagation = SUPPORTS, readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw NotFoundException("User not found")
        val grantedAuthorities = user.roles.map { toGrantedAuthority(it) }
        return User(username, user.password, grantedAuthorities)
    }

    private fun toGrantedAuthority(role: Role): SimpleGrantedAuthority {
        return SimpleGrantedAuthority(ROLE_PREFIX.plus(role.role))
    }

}