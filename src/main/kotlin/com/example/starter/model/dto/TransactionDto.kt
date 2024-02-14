package com.example.starter.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionDto(
  @JsonProperty("valor")
    val value: Int,
  @JsonProperty("tipo")
    val type: TypeEnum,
  @JsonProperty("descricao")
    val description: String
)

