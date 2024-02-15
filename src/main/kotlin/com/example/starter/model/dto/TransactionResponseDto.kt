package com.example.starter.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionResponseDto(
    @JsonProperty("limite")
    val limite: Int,
    @JsonProperty("saldo")
    val saldo: Int
)
