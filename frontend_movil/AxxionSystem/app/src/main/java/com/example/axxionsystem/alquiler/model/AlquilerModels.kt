package com.example.axxionsystem.alquiler.model

import java.math.BigDecimal

// ─── Enums ───────────────────────────────────────────

enum class EstadoSolicitud { Nueva, EnProceso, Atendida, Cancelada }
enum class EstadoRenta { Programada, EnCurso, Finalizada, Retrasada, Cancelada }
enum class EstadoEntrega { Pendiente, Enviada, Entregada, Fallida }
enum class EstadoDevolucion { Pendiente, EnProceso, Completada, Incompleta }

// ─── Solicitudes ─────────────────────────────────────

data class SolicitudCreateRequest(
    val clienteId: Int,
    val productos: List<Int>? = emptyList(),
    val nombreProductoAlternativo: String? = null,
    val cantidadSolicitada: Int? = 1,
    val descripcionNecesidad: String? = null
)

data class SolicitudResponse(
    val id: Int,
    val clienteId: Int,
    val fechaSolicitud: String?,
    val estado: EstadoSolicitud,
    val productos: List<Int>,
    val nombreProductoAlternativo: String?,
    val cantidadSolicitada: Int?,
    val descripcionNecesidad: String?
)

// ─── Rentas ──────────────────────────────────────────

data class RentaItemRequest(
    val inventarioItemId: Int,
    val precioRentaItem: BigDecimal,
    val condicionSalida: String? = null,
    val notas: String? = null
)

data class RentaCreateRequest(
    val clienteId: Int,
    val cotizacionId: Int? = null,
    val fechaInicio: String,          // ISO-8601: "2025-03-04T08:00:00"
    val fechaFinPrevista: String,     // ISO-8601
    val montoTotalRenta: BigDecimal? = null,
    val depositoGarantia: BigDecimal? = null,
    val notas: String? = null,
    val items: List<RentaItemRequest>
)

data class RentaItemResponse(
    val inventarioItemId: Int,
    val productoId: Int,
    val precioRentaItem: BigDecimal,
    val condicionSalida: String?,
    val condicionRegreso: String?,
    val notas: String?
)

data class RentaResponse(
    val id: Int,
    val clienteId: Int,
    val estado: EstadoRenta,
    val fechaInicio: String?,
    val fechaFinPrevista: String?,
    val fechaDevolucionReal: String?,
    val items: List<RentaItemResponse>
)

// ─── Entrega ─────────────────────────────────────────

data class CondicionItemRequest(
    val inventarioItemId: Int,
    val condicion: String? = null,
    val notas: String? = null
)

data class EntregaFirmaRequest(
    val rentaId: Int,
    val direccionId: Int,
    val fechaEnvio: String? = null,
    val companiaEnvio: String? = null,
    val numeroGuia: String? = null,
    val estadoEntrega: EstadoEntrega? = null,
    val firmaDigital: String? = null,
    val notas: String? = null,
    val condicionesSalida: List<CondicionItemRequest>? = emptyList()
)

data class EntregaResponse(
    val id: Int,
    val rentaId: Int,
    val direccionId: Int,
    val estado: EstadoEntrega,
    val fechaEnvio: String?,
    val notas: String?
)

// ─── Devolución ──────────────────────────────────────

data class DevolucionFirmaRequest(
    val rentaId: Int,
    val fechaDevolucionProgramada: String? = null,  // ISO LocalDate: "2025-03-04"
    val fechaDevolucionReal: String? = null,        // ISO LocalDateTime
    val estadoDevolucion: EstadoDevolucion? = null,
    val personaRecibe: String? = null,
    val firmaDigital: String? = null,
    val notasGenerales: String? = null,
    val condicionesRegreso: List<CondicionItemRequest>? = emptyList()
)

data class DevolucionResponse(
    val id: Int,
    val rentaId: Int,
    val estado: EstadoDevolucion,
    val fechaDevolucionReal: String?,
    val notasGenerales: String?
)
