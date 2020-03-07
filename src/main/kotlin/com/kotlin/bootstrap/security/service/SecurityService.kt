package com.kotlin.bootstrap.security.service

import com.kotlin.bootstrap.security.dao.LoginDAO
import com.kotlin.bootstrap.security.dao.SignupDAO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.exception.UserAlreadyExistsException
import com.kotlin.bootstrap.security.repository.UserRepository
import com.kotlin.bootstrap.security.utils.JWTUtils
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.Objects.nonNull
import kotlin.streams.toList

@Service
class SecurityService(var authenticationManager: AuthenticationManager, var userRepository: UserRepository, @Lazy var jwtUtils: JWTUtils, var encoder: PasswordEncoder) : UserDetailsService {

    companion object {
        val ROLE_PREFIX = "ROLE_"
    }

    fun authenticate(loginDAO: LoginDAO): String {
        // Try to authenticate the user with username and password
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginDAO.username, loginDAO.password))

        // Set the Security context with the authentication
        SecurityContextHolder.getContext().authentication = authentication

        // Generate the JWT Token
        val jwt = jwtUtils.generateJwtToken(authentication)

        return jwt;
    }

    fun createUser(signupDAO: SignupDAO): User {
        if (userExists(signupDAO.username, signupDAO.email)) {
            throw UserAlreadyExistsException()
        }

        var user = User(id = null, username = signupDAO.username, email = signupDAO.email, password = encoder.encode(signupDAO.password), roles = Collections.emptyList())

        user.roles = signupDAO.roles
                .stream()
                .map { role -> toRole(role, user) }
                .toList()

        return userRepository.save(user)
    }

    private fun toRole(role: String, user: User): Role {
        return Role(null, user, role)
    }

    private fun userExists(username: String, email: String): Boolean {
        return userRepository.existsByUsernameOrEmail(username, email)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
        return user.takeIf { user -> user != null }.let { user ->  toUserDetails(user!!) }
    }

    private fun toUserDetails(user: User): UserDetails {
        var authorities = user.roles.stream()
                .filter { role: Role -> nonNull(role) }
                .map { role: Role -> SimpleGrantedAuthority(ROLE_PREFIX + role.role) }
                .toList()
            return UserDetails(user.id, user.email, user.username, user.password, authorities)
    }
}