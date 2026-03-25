package com.example.axxionsystem.data.api

/**
 * Contrato de red (Retrofit) para el backend.
 *
 * Define endpoints de autenticacion: login, perfil del usuario, refresh de token
 * (sincrono para el Authenticator) y logout.
 */
import com.example.axxionsystem.data.model.auth.AuthResponse
import com.example.axxionsystem.data.model.auth.ForgotPasswordRequest
import com.example.axxionsystem.data.model.auth.LoginRequest
import com.example.axxionsystem.data.model.auth.ResetPasswordRequest
import com.example.axxionsystem.data.model.auth.UserProfileResponse
import com.example.axxionsystem.data.model.producto.ProductoEntity
import com.example.axxionsystem.data.model.producto.UpdateEstadoRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("/api/usuario/perfil")
    suspend fun getPerfil(): Response<UserProfileResponse>

    @POST("api/auth/refresh")
    fun refreshTokenSync(): Call<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Any>

    @POST("/api/auth/olvido-contraseña")
    suspend fun requestPasswordRecovery(@Body request: ForgotPasswordRequest): Response<Unit>

    @POST("/api/auth/resetear-contraseña")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    @GET("api/productos")
    suspend fun getProductos(): Response<List<ProductoEntity>>

    @PATCH("api/productos/{id}/estado")
    suspend fun updateEstadoProducto(
        @Path("id") id: Int,
        @Body request: UpdateEstadoRequest
    ): Response<ProductoEntity>
}
