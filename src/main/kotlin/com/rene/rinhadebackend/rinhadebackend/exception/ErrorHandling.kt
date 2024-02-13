package com.rene.rinhadebackend.rinhadebackend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@ControllerAdvice
class ErrorHandling {
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnprocessableEntityException::class)
    fun handleConstraintViolation(exception: Exception?): String {
        return "Request Inválida"
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exception: Exception?): String {
        return "Request Inválida"
    }
}