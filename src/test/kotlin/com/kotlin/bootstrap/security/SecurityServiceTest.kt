package com.kotlin.bootstrap.security

import com.kotlin.bootstrap.security.dto.SignupDTO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.repository.RoleRepository
import com.kotlin.bootstrap.security.repository.UserRepository
import com.kotlin.bootstrap.security.service.SecurityService
import com.kotlin.bootstrap.security.utils.JWTUtils
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Optional.of

@ExtendWith(SpringExtension::class)
class SecurityServiceTest {

    @InjectMocks
    lateinit var securityService : SecurityService

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var authenticationManager: AuthenticationManager

    @Mock
    lateinit var jwtUtils: JWTUtils

    @Mock
    lateinit var encoder: PasswordEncoder

    @Mock
    lateinit var roleRepository: RoleRepository

    @Test
    fun createUserSanity() {
        // Given
        var user = User(1L, "username", "email", "password", emptyList())
        var signupDTO = SignupDTO("username", "email", "password")

        // When
        whenever(userRepository.save(anyOrNull<User>())).thenReturn(user)
        whenever(encoder.encode(anyOrNull())).thenReturn("passwordEncoded")
        securityService.createUser(signupDTO)

        // Then
        verify(userRepository).save(Mockito.any(User::class.java))
        verify(encoder).encode(Mockito.anyString())
    }

    @Test
    fun addRoleSanity() {
        // Given
        var user = User(1L, "username", "email", "password", emptyList())

        // When
        whenever(userRepository.findByUsername(anyOrNull())).thenReturn(user)
        securityService.addRole("username", "ADMIN")

        // Then
        verify(roleRepository).save(anyOrNull<Role>())
    }

    @Test
    fun removeRoleSanity() {
        // Given
        var user = User(1L, "username", "email", "password", emptyList())

        // When
        whenever(userRepository.findByUsername(any())).thenReturn(user)
        securityService.removeRole("username", "ADMIN")

        // Then
        verify(roleRepository).deleteByUserAndRole(user = anyOrNull(), role = any())
    }

    @Test
    fun getUserByUsername() {
        // Given
        val user = User(1L, "username", "email", "password", emptyList())

        // When
        whenever(userRepository.findByUsername("username")).thenReturn(user)
        val userResponse = securityService.getUser("username")

        // Then
        assertThat(userResponse).isNotNull
        assertThat(userResponse.username).isEqualTo("username")
    }

    @Test
    fun getUserByUserId() {
        // Given
        val user = User(1L, "username", "email", "password", emptyList())

        // When
        whenever(userRepository.findById(1L)).thenReturn(of(user))
        val userResponse = securityService.getUser(1L)

        // Then
        assertThat(userResponse).isNotNull
        assertThat(userResponse.username).isEqualTo("username")
    }

    @Test
    fun getUsers() {
        // Given
        val user = listOf(User(1L, "username", "email", "password", emptyList()))

        // When
        whenever(userRepository.findAll(Mockito.any(Pageable::class.java))).thenReturn(PageImpl(user))
        val userResponse = securityService.getUser(Pageable.unpaged())

        // Then
        assertThat(userResponse.content).isNotEmpty
        assertThat(userResponse.totalElements).isEqualTo(1)
    }

}