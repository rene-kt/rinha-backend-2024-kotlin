package com.example.starter.model.entity

import java.time.LocalDateTime

data class Transaction(
    val id: Int = 0,
    val valor: Int,
    val tipo: Char,
    val descricao: String,
    val dataCriacao: LocalDateTime,
    val clienteId: Int
)
