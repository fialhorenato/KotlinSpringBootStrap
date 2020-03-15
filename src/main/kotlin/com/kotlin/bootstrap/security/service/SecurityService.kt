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
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
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

        // Generate the JWT Token
        val jwt = jwtUtils.generateJwtToken(authentication)

        return jwt;
    }

    fun createUser(signupDTO: SignupDTO): User {
        if (userExists(signupDTO.username, signupDTO.email)) {
            throw UserAlreadyExistsException()
        }

        var user = User(id = null, username = signupDTO.username, email = signupDTO.email, password = encoder.encode(signupDTO.password), roles = Collections.emptyList())

        user.roles = listOf(toRole("USER", user))

        return userRepository.save(user)
    }

    private fun toRole(role: String, user: User): Role {
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
        var authorities = user.roles.stream()
                .filter { role: Role -> nonNull(role) }
                .map { role: Role -> SimpleGrantedAuthority(ROLE_PREFIX + role.role) }
                .toList()

        var roles = user.roles.stream()
                .filter { role: Role -> nonNull(role) }
                .map { role -> role.role }
                .toList()

        return UserDetails(user.email, user.username, user.password, authorities, roles)
    }

    fun addRole(userId: String, role: String) {
        var role = Role(role = role, user = getUser(userId))
        roleRepository.save(role)
    }

    fun removeRole(userId: String, role: String) {
        roleRepository.deleteByUserAndRole(role = role, user = getUser(userId))

    }

    private fun getUser(userId: Long): User {
        return userRepository.findById(userId).orElseThrow(NotFoundException(String.format("User %d cannot be found", userId)))
    }

    private fun getUser(username: String): User {
        return userRepository.findByUsername(username) ?: throw NotFoundException(String.format("User %s cannot be found", username))
    }
}