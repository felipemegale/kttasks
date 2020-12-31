package com.kttasks.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.kttasks.dtos.UserDto
import java.util.*

class TokenService(
    private val issuer: String,
    private val audience: String,
    private val expirationTime: Long,
    private val jwtSecret: String
) {
    private val algorithm = Algorithm.HMAC256(this.jwtSecret)
    private fun getExpiresAt() = Date(System.currentTimeMillis() + this.expirationTime)

    fun buildJwtVerifier(): JWTVerifier = JWT.require(algorithm)
        .withIssuer(this.issuer)
        .build()

    fun generateToken(userInfo: UserDto): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(this.issuer)
            .withAudience(this.audience)
            .withClaim("name", userInfo.username)
            .withClaim("password", userInfo.password)
            .withExpiresAt(getExpiresAt())
            .sign(algorithm)
    }
}