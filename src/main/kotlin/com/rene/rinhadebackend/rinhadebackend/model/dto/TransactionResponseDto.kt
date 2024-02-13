package com.rene.rinhadebackend.rinhadebackend.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionResponseDto(
    @JsonProperty("limite")
    val limit: Int,
    @JsonProperty("saldo")
    val balance: Int
)
