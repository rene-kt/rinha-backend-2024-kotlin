package com.rene.rinhadebackend.rinhadebackend.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("clientes")
data class Client(
    @Id
    val id: Int,
    @Column("limite")
    var limit: Int,
    var saldo: Int
)