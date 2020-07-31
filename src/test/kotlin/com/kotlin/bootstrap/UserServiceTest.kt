package com.kotlin.bootstrap

import com.kotlin.bootstrap.factory.UserFactory.Companion.generateUser
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.repository.UserRepository
import com.kotlin.bootstrap.security.service.UserService
import com.kotlin.bootstrap.security.service.impl.UserServiceImpl
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    @InjectMocks
    lateinit var userService: UserServiceImpl

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun createSanity() {
        whenever(passwordEncoder.encode(anyString())).thenReturn("passwordEncoded")
        whenever(userRepository.save(any(User::class.java))).thenReturn(generateUser())
        userService.create("username", "password", "email")
        verify(userRepository).save(any(User::class.java))
        verify(passwordEncoder).encode(anyString())
    }

    @Test
    fun findByEmailSanity() {
        whenever(userRepository.findByEmail(anyString())).thenReturn(null, generateUser())

        // 1st time should throw NotFoundException
        assertThrows(NotFoundException::class.java) {
            userService.findByEmail("email")
        }

        // 2nd time should not be null
        assertNotNull(userService.findByEmail("email"))
    }

    @Test
    fun deleteByIdSanity() {
        userService.deleteById(1L)
        verify(userRepository).deleteById(any(Long::class.java))
    }

    @Test
    fun deleteByEmailSanity() {
        userService.deleteByEmail("email")
        verify(userRepository).deleteByEmail(anyString())
    }
}