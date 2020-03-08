package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dao.LoginDTO
import com.kotlin.bootstrap.security.dao.SignupDTO
import com.kotlin.bootstrap.security.dao.UserCreatedDTO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.service.SecurityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.streams.toList

@RestController
@RequestMapping(value = ["/security"])
class SecurityController(var securityService: SecurityService) {

    @PostMapping(value = ["/signup"])
    fun signup(@RequestBody signupDTO: SignupDTO): ResponseEntity<Any> {
        var user = securityService.createUser(signupDTO)
        var userCreatedDAO = UserCreatedDTO(user.username, user.password, user.roles.stream().map { t: Role ->  t.role }.toList())
        return ResponseEntity(userCreatedDAO, HttpStatus.CREATED)
    }

    @PostMapping(value = ["/login"])
    fun login(@RequestBody loginDTO: LoginDTO): String {
        return securityService.authenticate(loginDTO)
    }
}