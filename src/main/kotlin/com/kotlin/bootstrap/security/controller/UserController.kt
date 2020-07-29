package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dto.UserRequestDTO
import com.kotlin.bootstrap.security.dto.UserResponseDTO
import com.kotlin.bootstrap.security.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/create")
    fun create(@RequestBody userRequestDTO: UserRequestDTO): UserResponseDTO {
        val user = userService.create(userRequestDTO)
        return UserResponseDTO(user)
    }

    @GetMapping("/me")
    fun me(): UserResponseDTO {
        val user = userService.me()
        return UserResponseDTO(user)
    }

}