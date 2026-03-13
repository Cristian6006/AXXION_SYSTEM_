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
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.security.core.context.SecurityContextHolder
import com.example.axxionSystem.repository.UserRepository

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired lateinit var authService: AuthService
    @Autowired lateinit var userRepository: UserRepository

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
        return ResponseEntity.status(HttpStatus.CREATED).body(request)
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
            "tokenType" to "Besrer",
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

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue(name = "refresh_token", required = false) refreshToken: String?,
        response: HttpServletResponse
    ): ResponseEntity<Any> {
        if (refreshToken.isNullOrBlank()) {
            return ResponseEntity.status(401).body(mapOf("error" to "Refresh token no encontrado. Inicie sesion nuevamente"))
        }
        return try {
            val authResponse = authService.refreshAccessToken(refreshToken)

            val nuevaRefreshCookie = ResponseCookie.from("refresh_token", authResponse.refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .build()

            response.addHeader(HttpHeaders.SET_COOKIE, nuevaRefreshCookie.toString())

            ResponseEntity.ok(mapOf(
                "accessToken" to authResponse.accessToken,
                "tokenType" to "Bearer ",
                "expiresIn" to 900
            ))
        } catch (e: IllegalArgumentException) {
            val deleteCookie = ResponseCookie.from("refresh_token", "").maxAge(0).path("/api/auth/refresh").build()
            response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString())
            ResponseEntity.status(401).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val emailAutenticado = SecurityContextHolder.getContext().authentication.name
        val usuario = userRepository.findByEmail(emailAutenticado).orElse(null)

        if (usuario != null) {
            authService.logout(usuario.id!!)
        }

        val deleteCookie = ResponseCookie.from("refresh_token", "")
            .httpOnly(true)
            .secure(false)
            .path("/api/auth/refresh")
            .maxAge(0)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString())

        return ResponseEntity.ok(mapOf("mensaje" to "Sesion cerrada exitosamente"))
    }
}
