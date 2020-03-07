package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dao.LoginDAO
import com.kotlin.bootstrap.security.dao.SignupDAO
import com.kotlin.bootstrap.security.dao.UserCreatedDAO
import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.service.SecurityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.streams.toList

@RestController
@RequestMapping(value = ["/security"])
class SecurityController(var securityService: SecurityService) {

    @PostMapping(value = ["/signup"])
    fun signup(@RequestBody signupDAO: SignupDAO): ResponseEntity<Any> {
        var user = securityService.createUser(signupDAO)
        var userCreatedDAO = UserCreatedDAO(user.username, user.password, user.roles.stream().map { t: Role ->  t.role }.toList())
        return ResponseEntity(userCreatedDAO, HttpStatus.CREATED)
    }

    @PostMapping(value = ["/login"])
    fun login(@RequestBody loginDAO: LoginDAO): String {
        return securityService.authenticate(loginDAO)
    }
}