package com.rene.rinhadebackend.rinhadebackend.repository

import com.rene.rinhadebackend.rinhadebackend.model.entity.Client
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : R2dbcRepository<Client, Int>