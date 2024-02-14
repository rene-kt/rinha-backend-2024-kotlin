package com.example.starter.services

import com.example.starter.db.PostgresFactory
import com.example.starter.model.dto.*
import com.example.starter.model.entity.Client
import io.vertx.core.Future
import io.vertx.sqlclient.SqlConnection
import io.vertx.sqlclient.Tuple
import java.time.LocalDateTime


class ClientService {

  private val dbClient = PostgresFactory.pgPool

  fun createTransaction(id: Int, transactionDto: TransactionDto): ResponseObject {
    val client = getClient(id) ?: return ResponseObject(404, "Cliente não encontrado")

    if (transactionDto.description.length > 10) return ResponseObject(422, "Descrição muito longa")

    if (transactionDto.type == TypeEnum.d) {
      val newValue = client.saldo - transactionDto.value
      if ((newValue * -1) > client.limit) return ResponseObject(422, "Limite excedido")

      updateClient(newValue, id)
      handleTransaction(transactionDto, id)

      return ResponseObject(200, TransactionResponseDto(client.limit, newValue))
    }

    handleTransaction(transactionDto, id)
    return ResponseObject(200, TransactionResponseDto(client.limit, client.saldo))
  }

  fun getStatement(id: Int): ResponseObject {

    val client = getClient(id) ?: return ResponseObject(404, "Cliente não encontrado")
    val transactions = getLast10Transactions(id)

    return ResponseObject(
      200,
      BalanceResponseDto(
        balance = BalanceDto(client.saldo, LocalDateTime.now().toString(), client.limit),
        transactions = transactions
      )
    )
  }


  private fun getLast10Transactions(id: Int): List<TransactionItemDto> {
    val transactions = mutableListOf<TransactionItemDto>()

    dbClient.query(
      "select valor, tipo, descricao, data_criacao from transacoes where client_id = $id " +
        "ORDER BY data_criacao DESC limit 10"
    )
      .execute()
      .onSuccess { rowSet ->

        transactions.addAll(rowSet.map {
          TransactionItemDto(
            it.get(Int::class.java, "valor"),
            it.get(Char::class.java, "tipo"),
            it.get(String::class.java, "descricao"),
            it.get(LocalDateTime::class.java, "data_criacao").toString()
          )
        })
      }

    return transactions
  }


  private fun getClient(id: Int): Client? {
    var client: Client? = null
    dbClient.query("select id, limite, saldo from clientes where id = $id").execute()
      .onSuccess { result ->
        println("entrou aq")
        println(result)
        if (result.size() > 0)
          client = result.map {
            Client(
              it.get(Int::class.java, "id"),
              it.get(Int::class.java, "limite"),
              it.get(Int::class.java, "saldo")
            )
          }.first()
      }.onFailure {
        println(it)
        println(it.message)
        println(it.cause)
      }

    return client
  }

  private fun handleTransaction(transactionDto: TransactionDto, clientId: Int) {
    dbClient.withTransaction { transaction ->
      insertTransaction(transaction, transactionDto, clientId)
        .onSuccess { success ->
          if (success) {
            transaction.transaction().commit()
            transaction.close()
          } else {
            transaction.transaction().rollback()
          }
        }
        .onFailure {
          transaction.transaction().rollback()
          transaction.close()
        }
    }
  }

  private fun insertTransaction(transaction: SqlConnection, transactionData: TransactionDto, id: Int): Future<Boolean> {
    val future = Future.future { future ->
      val insertQuery =
        "INSERT INTO transacoes (valor, tipo, descricao, data_criacao, client_id) VALUES ($1, $2, $3, now(), $4)"
      val params = Tuple.of(transactionData.value, transactionData.type, transactionData.description, id)

      transaction.preparedQuery(insertQuery).execute(params).onComplete { ar ->
        if (ar.succeeded()) {
          future.complete(true)
        } else {
          future.fail(ar.cause())
        }
      }
    }

    return future
  }

  private fun updateClient(saldo: Int, clientId: Int): Future<Boolean> {
    val future = Future.future { future ->
      dbClient.query("UPDATE clientes SET saldo = $saldo WHERE id = $clientId").execute()
        .onSuccess { future.complete(true) }
        .onFailure { future.fail(it) }
    }

    return future
  }
}
