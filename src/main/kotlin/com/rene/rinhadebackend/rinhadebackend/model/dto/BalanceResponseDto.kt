package com.rene.rinhadebackend.rinhadebackend.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

class BalanceResponseDto(
    @JsonProperty("saldo")
    val balance: BalanceDto,
    @JsonProperty("ultimas_transacoes")
    val transactions: List<TransactionItemDto>
)