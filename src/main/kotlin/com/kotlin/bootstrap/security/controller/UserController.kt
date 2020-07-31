package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dto.user.UserRequestDTO
import com.kotlin.bootstrap.security.dto.user.UserResponseDTO
import com.kotlin.bootstrap.security.service.UserService
import com.kotlin.bootstrap.security.service.impl.UserServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/create")
    fun create(@RequestBody userRequestDTO: UserRequestDTO): UserResponseDTO {
        val user = userService.create(userRequestDTO.username, userRequestDTO.password, userRequestDTO.email)
        return UserResponseDTO(user)
    }

    @GetMapping("/me")
    fun me(): UserResponseDTO {
        val user = userService.me()
        return UserResponseDTO(user)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserById(@PathVariable("id") id : Long) {
        userService.deleteById(id)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/email/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserById(@PathVariable("email") email : String ) {
        userService.deleteByEmail(email)
    }

}