package com.example.starter.db

import io.vertx.core.Vertx
import io.vertx.pgclient.PgConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions


object PostgresFactory {

    private const val HOSTNAME = "postgres-db" // Alterado de "localhost" para "postgres-db"
    private const val DATABASE = "postgres"
    private const val USER = "postgres"
    private const val PASSWORD = "postgres"

    val pgPool: Pool by lazy {
        val connectOptions = PgConnectOptions()
            .setPort(5432)
            .setHost(HOSTNAME)
            .setDatabase(DATABASE)
            .setUser(USER)
            .setPassword(PASSWORD)

        Pool.pool(Vertx.vertx(), connectOptions, PoolOptions())
    }
}

