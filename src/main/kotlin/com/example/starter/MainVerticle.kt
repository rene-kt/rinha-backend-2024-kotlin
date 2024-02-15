package com.example.starter

import com.example.starter.model.dto.TransactionDto
import com.example.starter.services.ClientService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler


class MainVerticle : AbstractVerticle() {

    fun main(args: Array<String>) {
        vertx.deployVerticle(MainVerticle())
    }

    private val clientService: ClientService = ClientService()

    override fun start(startPromise: Promise<Void>) {
        val router: Router = Router.router(vertx)
        router.route().handler(BodyHandler.create())

        val mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)

        // GET
        router.get("/clientes/:id/extrato").handler {
            val result = clientService.getStatement(it.pathParam("id").toInt())

            it.response().setStatusCode(result.statusCode).putHeader("content-type", "application/json")
                .end(Json.encodePrettily(result.body))
        }

        // POST
        router.post("/clientes/:id/transacoes").handler {
            val id = it.pathParam("id").toInt()
            val body = it.body().asString()
            val obj = try {
                mapper.readValue(body, TransactionDto::class.java)
            } catch (e: InvalidFormatException) {
                it.response().setStatusCode(422).putHeader("content-type", "application/json")
                    .end(Json.encodePrettily("Requisição inválida"))
                return@handler
            }
            val result = clientService.createTransaction(
                id, obj
            )

            it.response().setStatusCode(result.statusCode).putHeader("content-type", "application/json")
                .end(Json.encodePrettily(result.body))
        }

        vertx.createHttpServer().requestHandler(router)
            .listen(8080) { http ->
                if (http.succeeded()) {
                    startPromise.complete()
                } else {
                    startPromise.fail(http.cause())
                }
            }
    }

}

