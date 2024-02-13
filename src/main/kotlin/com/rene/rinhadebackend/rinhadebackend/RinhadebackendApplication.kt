package com.rene.rinhadebackend.rinhadebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = ["com.rene.rinhadebackend.rinhadebackend.repository"])
@EnableWebFlux
class RinhadebackendApplication

fun main(args: Array<String>) {
	runApplication<RinhadebackendApplication>(*args)
}
