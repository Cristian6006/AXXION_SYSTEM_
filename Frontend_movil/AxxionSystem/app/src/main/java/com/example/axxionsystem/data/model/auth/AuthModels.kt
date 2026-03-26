package com.example.axxionsystem.data.model.auth

import com.example.axxionsystem.data.local.entity.UserEntity

/**
 * Modelos (DTOs) usados por la capa de red/autenticacion.
 *
 * - [LoginRequest]: payload de login.
 * - [AuthResponse]: respuesta con token y metadata.
 * - [UserProfileResponse]: datos basicos del perfil/roles.
 */
data class LoginRequest(
    val email: String,
    val password: String,
    val deviceName: String = "Android Device"
)

data class AuthResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int
)

data class UserProfileResponse(
    val id: Int,
    val nombre: String,
    val email: String,
    val roles: List<String>
)

fun UserProfileResponse.toEntity(): UserEntity {
    val rolLimpio = this.roles.firstOrNull()?.replace("ROLE_", "") ?: "USUARIO"

    return UserEntity(
        localId = 1,
        serverId = this.id,
        nombre = this.nombre,
        email = this.email,
        rol = rolLimpio
    )
}
