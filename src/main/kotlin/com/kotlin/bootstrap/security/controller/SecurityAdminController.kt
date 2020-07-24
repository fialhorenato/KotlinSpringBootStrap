package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dto.LoginDTO
import com.kotlin.bootstrap.security.dto.SignupDTO
import com.kotlin.bootstrap.security.dto.UserCreatedDTO
import com.kotlin.bootstrap.security.dto.UserResponseDTO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.service.SecurityService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import kotlin.streams.toList

@RestController
@RequestMapping(value = ["/security/admin"])
@PreAuthorize("hasRole('ROLE_ADMIN')")
class SecurityAdminController(var securityService: SecurityService) {

    @PostMapping("/user/{username}/role/{role}")
    fun addRole(@PathVariable(value = "username") username : String, @PathVariable(value = "role") role : String) : ResponseEntity<Any> {
        securityService.addRole(username, role)
        return ResponseEntity(CREATED)
    }

    @DeleteMapping("/user/{username}/role/{role}")
    fun removeRole(@PathVariable(value = "username") username : String, @PathVariable(value = "role") role : String) : ResponseEntity<Any> {
        securityService.removeRole(username, role)
        return ResponseEntity(OK)
    }

    @GetMapping("/user")
    fun getUser(pageable: Pageable): Page<UserResponseDTO> {
        val page = securityService.getUser(pageable)
        return PageImpl(page.get().map { r -> UserResponseDTO(r) }.toList(), page.pageable, page.totalElements)
    }

    @GetMapping("/user/username/{username}")
    fun getUser(@PathVariable(value = "username") username : String): UserResponseDTO {
        return UserResponseDTO(securityService.getUser(username = username))
    }

    @GetMapping("/user/user_id/{user_id}")
    fun getUser(@PathVariable(value = "user_id") userId : Long): UserResponseDTO {
        return UserResponseDTO(securityService.getUser(userId = userId))
    }

}