package com.example.starter.services

import com.example.starter.db.PostgresFactory
import com.example.starter.model.dto.*
import com.example.starter.model.entity.Client
import io.vertx.core.Future
import io.vertx.sqlclient.SqlConnection
import io.vertx.sqlclient.Tuple
import java.time.LocalDateTime
import kotlin.math.abs


class ClientService {

    private val dbClient = PostgresFactory.pgPool

    fun createTransaction(id: Int, transactionDto: TransactionDto): ResponseObject {
        if (
            transactionDto.descricao == null ||
            transactionDto.descricao.length > 10 ||
            transactionDto.descricao.isEmpty() ||
            transactionDto.tipo.isEmpty() ||
            transactionDto.tipo !in listOf("d", "c") ||
            transactionDto.valor <= 0
        ) return ResponseObject(
            422,
            "Requisição inválida"
        )

        return dbClient.withTransaction { transaction ->
            getClient(transaction, id)
                .compose { client ->
                    if (client == null)
                        return@compose Future.succeededFuture(ResponseObject(404, "Cliente não encontrado"))


                    if (transactionDto.tipo == "d") {
                        val newValue = client.saldo - transactionDto.valor
                        if (abs(newValue) > client.limit)
                            return@compose Future.succeededFuture(ResponseObject(422, "Limite excedido"))


                        return@compose updateClient(transaction, newValue, id)
                            .flatMap { insertTransaction(transaction, transactionDto, id) }
                            .map { ResponseObject(200, TransactionResponseDto(client.limit, newValue)) }
                    }


                    return@compose insertTransaction(transaction, transactionDto, id)
                        .map { ResponseObject(200, TransactionResponseDto(client.limit, client.saldo)) }

                }
        }.toCompletionStage()
            .toCompletableFuture()
            .get()
    }

    fun getStatement(id: Int): ResponseObject {
        return dbClient.withTransaction { transaction ->
            getClient(transaction, id).compose { client ->
                if (client == null) {
                    return@compose Future.succeededFuture(ResponseObject(404, "Cliente não encontrado"))
                }

                return@compose getLast10Transactions(transaction, id)
                    .map { transactions ->
                        val balanceDto = BalanceDto(client.saldo, LocalDateTime.now().toString(), client.limit)
                        val balanceResponseDto = BalanceResponseDto(balanceDto, transactions)
                        ResponseObject(200, balanceResponseDto)
                    }
            }
        }.toCompletionStage().toCompletableFuture().get()
    }

    private fun getLast10Transactions(transaction: SqlConnection, id: Int): Future<List<TransactionItemDto>> {
        val query = "SELECT * FROM transacoes WHERE cliente_id = $1 ORDER BY data_criacao DESC LIMIT 10"
        val tuples = Tuple.of(id)
        return transaction.preparedQuery(query)
            .execute(tuples).map { result ->
                result.map { row ->
                    TransactionItemDto(
                        row.getInteger("valor"),
                        row.getString("tipo")[0],
                        row.getString("descricao"),
                        row.getLocalDateTime("data_criacao").toString()
                    )
                }
            }
    }


    private fun getClient(transaction: SqlConnection, id: Int): Future<Client?> {
        val query = "SELECT * FROM clientes WHERE id = $1"
        val params = Tuple.of(id)
        return transaction.preparedQuery(query).execute(params)
            .map { result ->
                val iterator = result.iterator()
                if (iterator.hasNext()) {
                    val row = iterator.next()
                    Client(
                        id = id,
                        limit = row.getInteger("limite"),
                        saldo = row.getInteger("saldo")
                    )
                } else null
            }
    }

    private fun insertTransaction(
        transaction: SqlConnection,
        transactionData: TransactionDto,
        id: Int
    ): Future<Boolean> {
        return Future.future { future ->
            val insertQuery =
                "INSERT INTO transacoes (valor, tipo, descricao, data_criacao, cliente_id) VALUES ($1, $2, $3, now(), $4)"
            val params = Tuple.of(transactionData.valor, transactionData.tipo, transactionData.descricao, id)

            transaction.preparedQuery(insertQuery).execute(params).onComplete { ar ->
                future.complete(ar.succeeded())
            }
        }
    }

    private fun updateClient(transaction: SqlConnection, saldo: Int, clientId: Int): Future<Boolean> {
        return Future.future { future ->
            val query = "UPDATE clientes SET saldo = $1 WHERE id = $2"
            val params = Tuple.of(saldo, clientId)
            transaction.preparedQuery(query).execute(params).onComplete {
                future.complete(it.succeeded())
            }
        }
    }
}
