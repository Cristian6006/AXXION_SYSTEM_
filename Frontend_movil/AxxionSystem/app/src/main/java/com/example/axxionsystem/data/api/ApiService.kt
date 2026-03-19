package com.example.axxionsystem.data.api

/**
 * Contrato de red (Retrofit) para el backend.
 *
 * Define endpoints de autenticacion: login, perfil del usuario, refresh de token
 * (sincrono para el Authenticator) y logout.
 */
import com.example.axxionsystem.data.model.AuthResponse
import com.example.axxionsystem.data.model.LoginRequest
import com.example.axxionsystem.data.model.UserProfileResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("/api/usuario/perfil")
    suspend fun getPerfil(): Response<UserProfileResponse>

    @POST("api/auth/refresh")
    fun refreshTokenSync(): Call<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Any>
}
