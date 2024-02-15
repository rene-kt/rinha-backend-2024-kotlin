package com.example.starter.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BalanceDto(
    val total: Int,
    @JsonProperty("data_extrato")
    val data_extrato: String,
    @JsonProperty("limite")
    val limite: Int
)
