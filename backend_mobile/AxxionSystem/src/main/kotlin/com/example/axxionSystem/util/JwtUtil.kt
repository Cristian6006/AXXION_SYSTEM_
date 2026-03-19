package com.example.axxionSystem.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtUtil {

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    @Value("\${jwt.access-expiration}")
    var accessExpiration: Long = 0

    fun generateAccessToken(username: String): String {
        return buildToken(username, accessExpiration)
    }

    private fun buildToken(username: String, expiration: Long): String
    {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            println("ERROR JWT EXACTO: ${e.javaClass.simpleName} - ${e.message}")
             false
        }
    }

    fun extractUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}