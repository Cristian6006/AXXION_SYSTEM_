package com.example.axxionSystem.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "El nombre es obligatorio")
    val nombre: String,

    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El formato del correo no es valido")
    val email: String,

    @field:NotBlank(message = "El nombre de usuario es obligatorio")
    val nombreUsuario: String,

    @field:NotBlank(message = "El primer apellido es obligatorio")
    val apellido1: String,

    @field:NotBlank(message = "El segundo nombre es obligatorio")
    val nombre2: String,

    @field:NotBlank(message = "El segundo apellido es obligatorio")
    val apellido2: String,

    @field:NotBlank(message = "El telefono es obligatorio")
    val telefono: String,

    @field:NotBlank(message = "El nombre es obligatorio")
    val departamento: String,

    @field:NotBlank(message = "El nombre es obligatorio")
    val estado: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+\$",
        message = "La contraseña debe contener al menos una mayuscula, una minuscula y un numero"
    )
    val password: String,

    val deviceName: String? = "Unknown Device"
)

data class LoginRequest(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val password: String,

    val deviceName:String? = "Unknown Device"
)
