package com.example.axxionsystem.common.api

import com.example.axxionsystem.auth.model.AuthResponse
import com.example.axxionsystem.auth.model.LoginRequest
import com.example.axxionsystem.auth.model.RegisterRequest
import com.example.axxionsystem.auth.model.RegisterResponse
import com.example.axxionsystem.alquiler.model.SolicitudCreateRequest
import com.example.axxionsystem.alquiler.model.SolicitudResponse
import com.example.axxionsystem.alquiler.model.RentaCreateRequest
import com.example.axxionsystem.alquiler.model.RentaResponse
import com.example.axxionsystem.alquiler.model.EntregaFirmaRequest
import com.example.axxionsystem.alquiler.model.EntregaResponse
import com.example.axxionsystem.alquiler.model.DevolucionFirmaRequest
import com.example.axxionsystem.alquiler.model.DevolucionResponse
import com.example.axxionsystem.mantenimiento.model.MantenimientoCreateRequest
import com.example.axxionsystem.mantenimiento.model.MantenimientoUpdateRequest
import com.example.axxionsystem.mantenimiento.model.MantenimientoResponse
import com.example.axxionsystem.common.model.ProductoResponse
import com.example.axxionsystem.common.model.ClienteResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * ApiServicesKotlin es la interfaz principal de Retrofit que define todos los 
 * endpoints (rutas, métodos HTTP, parámetros y verbos) disponibles en el backend.
 * Está organizada funcionalmente en bloques:
 * - Autenticación (Login, Registro).
 * - Consultas Generales (Usuarios, Productos, Clientes).
 * - Gestión de Alquileres y Rentas (creación, listado y firma digital).
 * - Mantenimiento (solicitudes y edición de estados).
 */
interface ApiServicesKotlin {

    // ─── Auth (público, sin token) ────────────────────────────────────────
    @POST("/api/auth/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("/api/auth/registro")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    // ─── Usuarios (requiere token) ────────────────────────────────────────
    @GET("/usuarios")
    fun getPersonas(): Call<DataResponse>

    // ─── Alquiler / Solicitudes ───────────────────────────────────────────
    @POST("/api/alquiler/solicitudes")
    fun crearSolicitud(@Body request: SolicitudCreateRequest): Call<SolicitudResponse>

    @GET("/api/alquiler/solicitudes")
    fun consultarSolicitudes(
        @Query("estado") estado: String? = null,
        @Query("fecha") fecha: String? = null
    ): Call<List<SolicitudResponse>>

    // ─── Alquiler / Rentas ────────────────────────────────────────────────
    @POST("/api/alquiler/rentas")
    fun crearRenta(@Body request: RentaCreateRequest): Call<RentaResponse>

    @GET("/api/alquiler/rentas/cliente/{clienteId}")
    fun rentasPorCliente(@Path("clienteId") clienteId: Int): Call<List<RentaResponse>>

    // ─── Alquiler / Entrega ───────────────────────────────────────────────
    @POST("/api/alquiler/entregas/firmar")
    fun firmarEntrega(@Body request: EntregaFirmaRequest): Call<EntregaResponse>

    // ─── Alquiler / Devolución ────────────────────────────────────────────
    @POST("/api/alquiler/devoluciones/firmar")
    fun firmarDevolucion(@Body request: DevolucionFirmaRequest): Call<DevolucionResponse>

    // ─── Mantenimiento ────────────────────────────────────────────────────
    @POST("/api/mantenimiento/solicitudes")
    fun crearMantenimiento(@Body request: MantenimientoCreateRequest): Call<MantenimientoResponse>

    @GET("/api/mantenimiento/solicitudes")
    fun consultarMantenimientos(
        @Query("responsable") responsable: String? = null
    ): Call<List<MantenimientoResponse>>

    @PUT("/api/mantenimiento/solicitudes/{id}")
    fun actualizarMantenimiento(
        @Path("id") id: Int,
        @Body request: MantenimientoUpdateRequest
    ): Call<MantenimientoResponse>

    // ─── Catálogos (Productos y Clientes) ──────────────────────────────────
    @GET("/api/productos")
    fun getProductos(): Call<List<ProductoResponse>>

    @GET("/api/clientes")
    fun getClientes(): Call<List<ClienteResponse>>
}