package com.kotlin.bootstrap.error

import com.kotlin.bootstrap.security.exception.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler() {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleNotFoundException(ex: UserAlreadyExistsException): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.CONFLICT)
    }

}