package com.example.starter.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionItemDto(
    @JsonProperty("valor")
    val valor: Int,
    @JsonProperty("tipo")
    val tipo: Char,
    @JsonProperty("descricao")
    val descricao: String,
    @JsonProperty("realizada_em")
    val realizada_em: String
)
