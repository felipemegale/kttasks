package com.kttasks

import com.kttasks.routes.registerAccountRouting
import com.kttasks.services.TokenService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    Database.connect(
        environment.config.property("database.dburl").getString(),
        driver = environment.config.property("database.dbdriver").getString(),
        user = environment.config.property("database.username").getString(),
        password = environment.config.property("database.password").getString()
    )

    val tokenService = TokenService(
        environment.config.property("jwt.issuer").getString(),
        environment.config.property("jwt.audience").getString(),
        environment.config.property("jwt.expirationTime").getString().toLong(),
        environment.config.property("jwt.secret").getString()
    )
    val jwtRealm = environment.config.property("jwt.realm").getString()

    install(ContentNegotiation) {
        json()
    }
    install(Authentication) {
        jwt {
            realm = jwtRealm
            verifier(tokenService.buildJwtVerifier())
            validate { credential -> tokenService.validateCredential(credential) }
        }
    }
    install(CallLogging) { }

    registerAccountRouting(tokenService)
}