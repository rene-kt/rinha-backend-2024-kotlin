package com.rene.rinhadebackend.rinhadebackend.controller

import com.rene.rinhadebackend.rinhadebackend.model.dto.BalanceResponseDto
import com.rene.rinhadebackend.rinhadebackend.model.dto.TransactionDto
import com.rene.rinhadebackend.rinhadebackend.model.dto.TransactionResponseDto
import com.rene.rinhadebackend.rinhadebackend.services.BalanceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class BalanceController(
    private val balanceService: BalanceService
) {

    @PostMapping("/clientes/{id}/transacoes")
    fun createTransaction(
        @RequestBody transactionDto: TransactionDto,
        @PathVariable id: Int
    ): Mono<TransactionResponseDto> {
        return balanceService.createTransaction(transactionDto, id)
    }

    @GetMapping("/clientes/{id}/extrato")
    fun getStatement(
        @PathVariable id: Int
    ): Mono<BalanceResponseDto> {
        return balanceService.getStatement(id)
    }

}