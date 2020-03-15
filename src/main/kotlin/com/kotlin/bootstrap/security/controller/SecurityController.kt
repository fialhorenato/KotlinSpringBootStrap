package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dto.LoginDTO
import com.kotlin.bootstrap.security.dto.SignupDTO
import com.kotlin.bootstrap.security.dto.UserCreatedDTO
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
        var userCreatedDAO = UserCreatedDTO(user.username, user.password, user.roles.stream().map { t: Role ->  t.role }.toList())
        return ResponseEntity(userCreatedDAO, CREATED)
    }

    @PostMapping(value = ["/login"])
    fun login(@RequestBody loginDTO: LoginDTO): String {
        return securityService.authenticate(loginDTO)
    }

    @PostMapping("/user/{username}/role/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun addRole(@PathVariable(value = "username") username : String, @PathVariable(value = "role") role : String) : ResponseEntity<Any> {
        securityService.addRole(username, role)
        return ResponseEntity(CREATED)
    }

    @DeleteMapping("/user/{username}/role/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun removeRole(@PathVariable(value = "username") username : String, @PathVariable(value = "role") role : String) : ResponseEntity<Any> {
        securityService.removeRole(username, role)
        return ResponseEntity(OK)
    }
}