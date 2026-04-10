package com.example.axxionsystem.data.api

/**
 * Contrato de red (Retrofit) para el backend.
 *
 * Define endpoints de:
 * - Autenticación: login, perfil, refresh token, logout
 * - Alquiler: solicitudes, rentas, firmas de entrega y devolución
 */
import com.example.axxionsystem.data.model.Alquiler.DevolucionFirmaRequest
import com.example.axxionsystem.data.model.Alquiler.DevolucionResponse
import com.example.axxionsystem.data.model.Alquiler.EntregaFirmaRequest
import com.example.axxionsystem.data.model.Alquiler.EntregaResponse
import com.example.axxionsystem.data.model.Alquiler.RentaCreateRequest
import com.example.axxionsystem.data.model.Alquiler.RentaResponse
import com.example.axxionsystem.data.model.Alquiler.SolicitudCreateRequest
import com.example.axxionsystem.data.model.Alquiler.SolicitudResponse
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
import retrofit2.http.PUT
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    // ═══════════════════════════════════════════════════════
    // MANTENIMIENTO
    // ═══════════════════════════════════════════════════════

    /**
     * Crea una nueva solicitud de mantenimiento.
     */
    @POST("/api/mantenimiento/solicitudes")
    suspend fun crearSolicitudMantenimiento(
        @Body request: com.example.axxionsystem.data.model.mantenimiento.MantenimientoCreateRequest
    ): Response<com.example.axxionsystem.data.model.mantenimiento.MantenimientoResponse>

    /**
     * Consulta las solicitudes de mantenimiento.
     * @param responsable Filtrar por responsable (opcional)
     */
    @GET("/api/mantenimiento/solicitudes")
    suspend fun consultarSolicitudesMantenimiento(
        @Query("responsable") responsable: String? = null
    ): Response<List<com.example.axxionsystem.data.model.mantenimiento.MantenimientoResponse>>

    /**
     * Actualiza una solicitud de mantenimiento.
     */
    @PUT("/api/mantenimiento/solicitudes/{id}")
    suspend fun actualizarSolicitudMantenimiento(
        @Path("id") id: Int,
        @Body request: com.example.axxionsystem.data.model.mantenimiento.MantenimientoUpdateRequest
    ): Response<com.example.axxionsystem.data.model.mantenimiento.MantenimientoResponse>
}
