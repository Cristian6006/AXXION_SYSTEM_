package com.example.axxionsystem.auth.model

data class LoginRequest(
    val email: String,
    val password: String,
    val deviceName: String = "Android App"
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val nombreUsuario: String,
    val apellido1: String,
    val nombre2: String,
    val apellido2: String,
    val telefono: String,
    val departamento: String,
    val estado: String,
    val password: String,
    val deviceName: String = "Android App"
)

data class AuthResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int
)

data class RegisterResponse(
    val Mensaje: String,
    val id: Int?
)
