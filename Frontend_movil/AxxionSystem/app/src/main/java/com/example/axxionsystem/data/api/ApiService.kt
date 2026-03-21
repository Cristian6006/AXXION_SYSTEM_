package com.example.axxionsystem.data.api

/**
 * Contrato de red (Retrofit) para el backend.
 *
 * Define endpoints de:
 * - Autenticación: login, perfil, refresh token, logout
 * - Alquiler: solicitudes, rentas, firmas de entrega y devolución
 */
import com.example.axxionsystem.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ═══════════════════════════════════════════════════════
    // AUTENTICACIÓN
    // ═══════════════════════════════════════════════════════

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("/api/usuario/perfil")
    suspend fun getPerfil(): Response<UserProfileResponse>

    @POST("api/auth/refresh")
    fun refreshTokenSync(): Call<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Any>

    // ═══════════════════════════════════════════════════════
    // ALQUILER - SOLICITUDES
    // ═══════════════════════════════════════════════════════

    /**
     * Crea una nueva solicitud de alquiler.
     */
    @POST("/api/alquiler/solicitudes")
    suspend fun crearSolicitud(
        @Body request: SolicitudCreateRequest
    ): Response<SolicitudResponse>

    /**
     * Consulta las solicitudes de alquiler.
     * @param estado Filtrar por estado (opcional)
     * @param fecha Filtrar por fecha (opcional)
     */
    @GET("/api/alquiler/solicitudes")
    suspend fun consultarSolicitudes(
        @Query("estado") estado: String? = null,
        @Query("fecha") fecha: String? = null
    ): Response<List<SolicitudResponse>>

    // ═══════════════════════════════════════════════════════
    // ALQUILER - RENTAS
    // ═══════════════════════════════════════════════════════

    /**
     * Crea una nueva renta.
     */
    @POST("/api/alquiler/rentas")
    suspend fun crearRenta(
        @Body request: RentaCreateRequest
    ): Response<RentaResponse>

    /**
     * Consulta las rentas de un cliente específico.
     */
    @GET("/api/alquiler/rentas/cliente/{clienteId}")
    suspend fun rentasPorCliente(
        @Path("clienteId") clienteId: Int
    ): Response<List<RentaResponse>>

    // ═══════════════════════════════════════════════════════
    // ALQUILER - ENTREGA
    // ═══════════════════════════════════════════════════════

    /**
     * Firma la entrega de una renta (en dirección de obra).
     */
    @POST("/api/alquiler/entregas/firmar")
    suspend fun firmarEntrega(
        @Body request: EntregaFirmaRequest
    ): Response<EntregaResponse>

    // ═══════════════════════════════════════════════════════
    // ALQUILER - DEVOLUCIÓN
    // ═══════════════════════════════════════════════════════

    /**
     * Firma la devolución de una renta.
     */
    @POST("/api/alquiler/devoluciones/firmar")
    suspend fun firmarDevolucion(
        @Body request: DevolucionFirmaRequest
    ): Response<DevolucionResponse>
}
