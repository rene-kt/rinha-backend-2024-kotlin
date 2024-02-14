package com.example.starter.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BalanceDto(
    val total: Int,
    @JsonProperty("data_extrato")
    val extractDate: String,
    @JsonProperty("limite")
    val limit: Int
)
