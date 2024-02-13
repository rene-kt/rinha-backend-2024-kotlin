package com.rene.rinhadebackend.rinhadebackend.exception

class UnprocessableEntityException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}