package com.kotlin.bootstrap.error

import com.kotlin.bootstrap.error.dto.GeneralResponseFailure
import com.kotlin.bootstrap.security.exception.NotFoundException
import com.kotlin.bootstrap.security.exception.UserAlreadyExistsException
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.util.NestedServletException
import java.lang.reflect.UndeclaredThrowableException

@RestControllerAdvice
class ErrorHandler() {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.localizedMessage), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.localizedMessage), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.localizedMessage),HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.errorCode.toString()),HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.mostSpecificCause.localizedMessage),HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(NestedServletException::class)
    fun handleNestedServletException(ex: NestedServletException): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.rootCause.localizedMessage),HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UndeclaredThrowableException::class)
    fun handleUndeclaredThrowableException(ex: UndeclaredThrowableException): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.undeclaredThrowable.cause?.localizedMessage ?: ex.localizedMessage),HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun defaultHandler(ex: Exception): ResponseEntity<Any> {
        return ResponseEntity(generateErrorResponse(ex.localizedMessage), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun generateErrorResponse(vararg messages : String) : GeneralResponseFailure {
        return GeneralResponseFailure(messages.toList());
    }
}