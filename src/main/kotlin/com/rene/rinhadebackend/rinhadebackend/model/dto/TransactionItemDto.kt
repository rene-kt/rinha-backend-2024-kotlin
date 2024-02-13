package com.rene.rinhadebackend.rinhadebackend.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionItemDto(
    @JsonProperty("valor")
    val value: Int,
    @JsonProperty("tipo")
    val type: Char,
    @JsonProperty("descricao")
    val description: String,
    @JsonProperty("realizada_em")
    val performedAt: String
)