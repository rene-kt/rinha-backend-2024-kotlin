package com.rene.rinhadebackend.rinhadebackend.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("transacoes")
data class Transaction(
    @Id
    val id: Int = 0,
    val valor: Int,
    val tipo: Char,
    val descricao: String,
    @Column("data_criacao")
    val dataCriacao: LocalDateTime,
    @Column("cliente_id")
    val clienteId: Int
)