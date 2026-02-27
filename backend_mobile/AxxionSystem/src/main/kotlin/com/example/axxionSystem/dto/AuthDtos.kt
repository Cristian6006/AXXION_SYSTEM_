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

data class UserProfileResponse(
    val id: Int?,
    val nombreUsuario: String,
    val nombre: String,
    val nombre2: String,
    val apellido1: String,
    val apellido2: String,
    val telefono: String?,
    val departamento: String?,
    val estado: String?,
    val email: String,
    val roles: List<String>
)

data class ForgotPasswordRequest (
    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "Formato de correo invalido")
    val email: String
)

data class ResetPasswordRequest (
    @field:NotBlank(message = "El código PIN es obligatorio")
    val token: String,
    @field:NotBlank(message = "La nueva contraseña es obligatoria")
    @field:Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Debe contener una mayúscula, una minúscula y un número"
    )
    val nuevaPassword: String
)
