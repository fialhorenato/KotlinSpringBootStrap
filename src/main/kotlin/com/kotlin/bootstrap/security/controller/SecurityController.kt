package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dto.LoginDTO
import com.kotlin.bootstrap.security.dto.SignupDTO
import com.kotlin.bootstrap.security.dto.UserCreatedDTO
import com.kotlin.bootstrap.security.dto.UserResponseDTO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.service.SecurityService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import kotlin.streams.toList

@RestController
@RequestMapping(value = ["/security"])
class SecurityController(var securityService: SecurityService) {

    @PostMapping(value = ["/signup"])
    fun signup(@RequestBody signupDTO: SignupDTO): ResponseEntity<Any> {
        var user = securityService.createUser(signupDTO)
        var userCreatedDAO = UserCreatedDTO(user)
        return ResponseEntity(userCreatedDAO, CREATED)
    }

    @PostMapping(value = ["/login"])
    fun login(@RequestBody loginDTO: LoginDTO): String {
        return securityService.authenticate(loginDTO)
    }

    @GetMapping(value = ["/me"])
    fun me(): UserResponseDTO {
        return UserResponseDTO(securityService.me())
    }


}