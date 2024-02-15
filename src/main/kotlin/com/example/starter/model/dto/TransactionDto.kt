package com.example.starter.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionDto(
  @JsonProperty("valor")
    val valor: Int,
  @JsonProperty("tipo")
    val tipo: String,
  @JsonProperty("descricao")
    val descricao: String?
)

