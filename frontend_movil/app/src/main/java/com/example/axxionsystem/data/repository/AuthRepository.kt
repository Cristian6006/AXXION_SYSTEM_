package com.example.axxionsystem.data.repository

/**
 * Repositorio de autenticacion.
 *
 * Centraliza el acceso a [ApiService] para que el ViewModel consuma un API
 * simple y testable (login, perfil y logout).
 */
import com.example.axxionsystem.data.api.ApiService
import com.example.axxionsystem.data.model.AuthResponse
import com.example.axxionsystem.data.model.LoginRequest
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return apiService.login(request)
    }

    suspend fun getPerfil() = apiService.getPerfil()

    suspend fun logout() = apiService.logout()
}
