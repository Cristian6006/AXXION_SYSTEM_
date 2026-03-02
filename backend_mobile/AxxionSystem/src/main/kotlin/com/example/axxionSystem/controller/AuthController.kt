package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.BiometricLoginRequest
import com.example.axxionSystem.dto.ForgotPasswordRequest
import com.example.axxionSystem.dto.LoginRequest
import com.example.axxionSystem.dto.RegisterRequest
import com.example.axxionSystem.dto.ResetPasswordRequest
import com.example.axxionSystem.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
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

    @PostMapping("/olvido-contraseña")
    fun forgotPassword(@Valid @RequestBody request: ForgotPasswordRequest): ResponseEntity<Any> {
        authService.solicitarRecuperacionPassword(request)
        return ResponseEntity.ok(mapOf("mensaje" to "Si el correo existe, hemos enviado un PIN de 6 digitos"))
    }

    @PostMapping("/resetear-contraseña")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest): ResponseEntity<Any> {
        authService.restablecerPassword(request)
        return ResponseEntity.ok(mapOf("mensaje" to "Contraseña actualizada exitosamente"))
    }

    @PostMapping("/registro")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Any> {
        authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body("")
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

    @PostMapping("/login-biometrico")
    fun loginBiometrico(@Valid @RequestBody request: BiometricLoginRequest, response: HttpServletResponse): ResponseEntity<Any> {

        val authResponse = authService.loginBiometrico(request)

        val refreshCookie = ResponseCookie.from("refresh_token", authResponse.refreshToken)
            .httpOnly(true)
            .secure(false)
            .path("/api/auth/refresh")
            .maxAge(7 * 24 * 60 * 60)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())

        return ResponseEntity.ok(mapOf(
            "mensaje" to "Login biométrico exitoso",
            "accessToken" to authResponse.accessToken,
            "tokenType" to "Bearer"
        ))
    }
}