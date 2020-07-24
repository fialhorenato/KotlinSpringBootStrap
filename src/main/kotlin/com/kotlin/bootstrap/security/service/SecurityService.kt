package com.kotlin.bootstrap.security.service

import com.kotlin.bootstrap.security.dto.LoginDTO
import com.kotlin.bootstrap.security.dto.SignupDTO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.exception.UserAlreadyExistsException
import com.kotlin.bootstrap.security.repository.RoleRepository
import com.kotlin.bootstrap.security.repository.UserRepository
import com.kotlin.bootstrap.security.utils.JWTUtils
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Collections.emptyList
import java.util.Objects.nonNull
import javax.transaction.Transactional
import kotlin.streams.toList

@Service
class SecurityService(
        var authenticationManager: AuthenticationManager,
        var userRepository: UserRepository,
        @Lazy var jwtUtils: JWTUtils,
        var encoder: PasswordEncoder,
        var roleRepository: RoleRepository
) : UserDetailsService {

    companion object {
        const val ROLE_PREFIX = "ROLE_"
    }

    fun authenticate(loginDTO: LoginDTO): String {
        // Try to authenticate the user with username and password
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginDTO.username, loginDTO.password))

        // Set the Security context with the authentication
        SecurityContextHolder.getContext().authentication = authentication

        // Generate and return the JWT Token
        return jwtUtils.generateJwtToken(authentication)
    }

    fun me(): UserDetails {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication.principal is UserDetails) {
            authentication.principal as UserDetails
        } else {
            throw AccessDeniedException("User not authenticated")
        }
    }

    fun createUser(signupDTO: SignupDTO): User {
        if (userExists(signupDTO.username, signupDTO.email)) {
            throw UserAlreadyExistsException()
        }

        val user = User(id = null, username = signupDTO.username, email = signupDTO.email, password = encoder.encode(signupDTO.password), roles = emptyList())

        user.roles = listOf(toRole(user =  user))

        return userRepository.save(user)
    }

    private fun toRole(role: String = "USER", user: User): Role {
        return Role(null, user, role)
    }

    private fun userExists(username: String, email: String): Boolean {
        return userRepository.existsByUsernameOrEmail(username, email)
    }

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
        return user?.let { toUserDetails(it) } ?: throw UsernameNotFoundException(String.format("Username %s not found", username))
    }

    private fun toUserDetails(user: User): UserDetails {
        val authorities = user.roles.stream()
                .filter { role: Role -> nonNull(role) }
                .map { role: Role -> SimpleGrantedAuthority(ROLE_PREFIX + role.role) }
                .toList()

        val roles = user.roles
                .filter { nonNull(it) }
                .map { it.role }
                .toList()

        return UserDetails(user.email, user.username, user.password, authorities, roles)
    }

    fun addRole(username: String, role: String) {
        roleRepository.save(Role(role = role, user = getUser(username)))
    }

    fun removeRole(userId: String, role: String) {
        roleRepository.deleteByUserAndRole(role = role, user = getUser(userId))
    }

    fun getUser(userId: Long): User {
        return userRepository.findById(userId).orElseThrow(NotFoundException(String.format("User %d cannot be found", userId)))
    }

    fun getUser(username: String): User {
        return userRepository.findByUsername(username) ?: throw NotFoundException(String.format("User %s cannot be found", username))
    }

    fun getUser(pageable : Pageable): Page<User> {
        return userRepository.findAll(pageable)
    }
}