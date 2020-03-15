package com.kotlin.bootstrap.error

import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.exception.UserAlreadyExistsException
import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler() {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.CONFLICT)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }


}