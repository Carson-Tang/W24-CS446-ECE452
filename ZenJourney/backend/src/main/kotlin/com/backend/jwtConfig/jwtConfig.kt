package com.backend.jwtConfig

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import user.UserRequest
import java.util.Date

object JwtConfig{
    private val secret = System.getenv("JWT_SECRET")
    private val issuer = "http://0.0.0.0:8080/"
    private const val validityInMs = 36_000_00 * 24 // 1 day
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    /**
     * Produce a token for this combination of name and password
     */
    fun generateToken(user: UserRequest): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("name", user.name)
        .withClaim("password", user.password)
        .withExpiresAt(getExpiration())  // optional
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}