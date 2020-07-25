package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dto.UserRequestDTO
import com.kotlin.bootstrap.security.dto.UserResponseDTO
import com.kotlin.bootstrap.security.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/create")
    fun create(@RequestBody userRequestDTO: UserRequestDTO): UserResponseDTO {
        val user = userService.create(userRequestDTO)
        return UserResponseDTO(
                username = user.username,
                email = user.email,
                roles = user.roles.map { it.role }
        )
    }

}