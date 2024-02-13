package com.rene.rinhadebackend.rinhadebackend.services

import com.rene.rinhadebackend.rinhadebackend.exception.NotFoundException
import com.rene.rinhadebackend.rinhadebackend.exception.UnprocessableEntityException
import com.rene.rinhadebackend.rinhadebackend.model.dto.*
import com.rene.rinhadebackend.rinhadebackend.model.entity.Client
import com.rene.rinhadebackend.rinhadebackend.model.entity.Transaction
import com.rene.rinhadebackend.rinhadebackend.repository.ClientRepository
import com.rene.rinhadebackend.rinhadebackend.repository.TransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class BalanceService(
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository
) {

    private fun getClient(id: Int): Mono<Client> {
        return clientRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Cliente não encontrado")))
    }

    fun createTransaction(transactionDto: TransactionDto, id: Int): Mono<TransactionResponseDto> {
        return getClient(id).flatMap {
            if (transactionDto.type == TypeEnum.d) {
                val newValue = it.saldo - transactionDto.value
                if ((newValue * -1) > it.limit) return@flatMap Mono.error(UnprocessableEntityException("Limite excedido"))

                it.saldo = newValue
            }

            val newTransaction = Transaction(
                valor = transactionDto.value,
                tipo = transactionDto.type.name[0],
                descricao = transactionDto.description,
                dataCriacao = LocalDateTime.now(),
                clienteId = it.id
            )

            transactionRepository.save(newTransaction)
                .then(clientRepository.save(it)).thenReturn(
                    TransactionResponseDto(
                        it.limit, it.saldo
                    )
                )
        }
    }

    fun getStatement(id: Int): Mono<BalanceResponseDto> {
        return getClient(id).flatMap { client ->
            transactionRepository.findAllTop10ByClienteIdOrderByDataCriacaoDesc(id)
                .collectList()
                .map { transactions ->
                    BalanceResponseDto(
                        BalanceDto(client.saldo, LocalDateTime.now().toString(), client.limit),
                        transactions.map {
                            TransactionItemDto(
                                it.valor, it.tipo, it.descricao,
                                it.dataCriacao.toString()
                            )
                        }
                    )
                }
        }
    }
}