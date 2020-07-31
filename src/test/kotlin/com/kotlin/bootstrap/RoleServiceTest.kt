package com.kotlin.bootstrap

import com.kotlin.bootstrap.factory.UserFactory.Companion.generateRole
import com.kotlin.bootstrap.factory.UserFactory.Companion.generateUser
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.repository.RoleRepository
import com.kotlin.bootstrap.security.repository.UserRepository
import com.kotlin.bootstrap.security.service.RoleService
import com.kotlin.bootstrap.security.service.UserService
import com.kotlin.bootstrap.security.service.impl.RoleServiceImpl
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
class RoleServiceTest {
    @InjectMocks
    lateinit var roleService: RoleServiceImpl

    @Mock
    lateinit var roleRepository: RoleRepository

    @Mock
    lateinit var userService: UserService

    @Test
    fun addSanity() {
        whenever(userService.findByEmail(anyString())).thenReturn(null, generateUser())
        whenever(roleRepository.save(any(Role::class.java))).thenReturn(generateRole(null))
        roleService.addRoleByEmail("email", "ROLE")
    }

    @Test
    fun removeSanity() {
        roleService.removeRoleByEmail("email", "ROLE")
        verify(roleRepository).deleteByUser_EmailAndRole(anyString(), anyString())
    }


}