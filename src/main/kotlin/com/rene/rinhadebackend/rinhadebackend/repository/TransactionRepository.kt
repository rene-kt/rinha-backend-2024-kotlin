package com.rene.rinhadebackend.rinhadebackend.repository

import com.rene.rinhadebackend.rinhadebackend.model.entity.Transaction
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface TransactionRepository : R2dbcRepository<Transaction, Int> {
    @Query("SELECT * FROM transacoes WHERE cliente_id = :clientId" +
            " ORDER BY data_criacao DESC LIMIT 10")
    fun findAllTop10ByClienteIdOrderByDataCriacaoDesc(clientId: Int): Flux<Transaction>
}