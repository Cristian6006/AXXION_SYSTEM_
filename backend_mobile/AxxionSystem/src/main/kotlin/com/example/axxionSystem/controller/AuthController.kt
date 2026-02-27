package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.LoginRequest
import com.example.axxionSystem.dto.RegisterRequest
import com.example.axxionSystem.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired lateinit var authService: AuthService

    @PostMapping("/registro")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Any> {
        val user = authService.register(request)
        return ResponseEntity.ok(mapOf("Mensaje" to "Usuario registrado exitosamente", "id" to user.id))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest, response: HttpServletResponse): ResponseEntity<Any> {
        val authResponse = authService.login(request)

        val refreshCookie = ResponseCookie.from("refresh_token", authResponse.refreshToken)
            .httpOnly(true)
            .secure(false)
            .path("/api/auth/refresh")
            .maxAge(7*24*60*60)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())

        return ResponseEntity.ok(mapOf(
            "accessToken" to authResponse.accessToken,
            "tokenType" to "Bearer",
            "expiresIn" to 900
        ))
    }
}