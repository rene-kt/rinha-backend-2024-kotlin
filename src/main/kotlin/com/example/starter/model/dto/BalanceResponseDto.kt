package com.example.starter.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

class BalanceResponseDto(
  @JsonProperty("saldo")
    val saldo: BalanceDto,
  @JsonProperty("ultimas_transacoes")
    val ultimas_transacoes: List<TransactionItemDto>
)
