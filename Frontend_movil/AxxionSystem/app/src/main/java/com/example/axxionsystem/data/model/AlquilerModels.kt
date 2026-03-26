package com.example.axxionsystem.data.model

import java.math.BigDecimal

// ─── Enums ───────────────────────────────────────────

/**
 * Estados posibles de una solicitud de alquiler.
 */
enum class EstadoSolicitud { Nueva, EnProceso, Atendida, Cancelada }

/**
 * Estados posibles de una renta.
 */
enum class EstadoRenta { Programada, EnCurso, Finalizada, Retrasada, Cancelada }

/**
 * Estados de entrega.
 */
enum class EstadoEntrega { Pendiente, Enviada, Entregada, Fallida }

/**
 * Estados de devolución.
 */
enum class EstadoDevolucion { Pendiente, EnProceso, Completada, Incompleta }

// ─── Solicitudes ─────────────────────────────────────

/**
 * Request para crear una nueva solicitud de alquiler.
 */
data class SolicitudCreateRequest(
    val clienteId: Int,
    val productos: List<Int>? = emptyList(),
    val nombreProductoAlternativo: String? = null,
    val cantidadSolicitada: Int? = 1,
    val descripcionNecesidad: String? = null
)

/**
 * Response del servidor con datos de una solicitud.
 */
data class SolicitudResponse(
    val id: Int,
    val clienteId: Int,
    val fechaSolicitud: String?,
    val estado: com.example.axxionsystem.data.model.Alquiler.EstadoSolicitud,
    val productos: List<Int>,
    val nombreProductoAlternativo: String?,
    val cantidadSolicitada: Int?,
    val descripcionNecesidad: String?
)

// ─── Rentas ──────────────────────────────────────────

/**
 * Request para crear un item de renta.
 */
data class RentaItemRequest(
    val inventarioItemId: Int,
    val precioRentaItem: BigDecimal,
    val condicionSalida: String? = null,
    val notas: String? = null
)

/**
 * Request para crear una renta completa.
 */
data class RentaCreateRequest(
    val clienteId: Int,
    val cotizacionId: Int? = null,
    val fechaInicio: String,
    val fechaFinPrevista: String,
    val montoTotalRenta: BigDecimal? = null,
    val depositoGarantia: BigDecimal? = null,
    val notas: String? = null,
    val items: List<com.example.axxionsystem.data.model.Alquiler.RentaItemRequest>
)

/**
 * Response de un item dentro de una renta.
 */
data class RentaItemResponse(
    val inventarioItemId: Int,
    val productoId: Int,
    val precioRentaItem: BigDecimal,
    val condicionSalida: String?,
    val condicionRegreso: String?,
    val notas: String?
)

/**
 * Response del servidor con datos de una renta.
 */
data class RentaResponse(
    val id: Int,
    val clienteId: Int,
    val estado: com.example.axxionsystem.data.model.Alquiler.EstadoRenta,
    val fechaInicio: String?,
    val fechaFinPrevista: String?,
    val fechaDevolucionReal: String?,
    val items: List<com.example.axxionsystem.data.model.Alquiler.RentaItemResponse>
)

// ─── Entrega ─────────────────────────────────────────

/**
 * Condición de un item en la entrega.
 */
data class CondicionItemRequest(
    val inventarioItemId: Int,
    val condicion: String? = null,
    val notas: String? = null
)

/**
 * Request para firmar la entrega de una renta.
 */
data class EntregaFirmaRequest(
    val rentaId: Int,
    val direccionId: Int,
    val fechaEnvio: String? = null,
    val companiaEnvio: String? = null,
    val numeroGuia: String? = null,
    val estadoEntrega: com.example.axxionsystem.data.model.Alquiler.EstadoEntrega? = null,
    val firmaDigital: String? = null,
    val notas: String? = null,
    val condicionesSalida: List<com.example.axxionsystem.data.model.Alquiler.CondicionItemRequest>? = emptyList()
)

/**
 * Response después de firmar entrega.
 */
data class EntregaResponse(
    val id: Int,
    val rentaId: Int,
    val direccionId: Int,
    val estado: com.example.axxionsystem.data.model.Alquiler.EstadoEntrega,
    val fechaEnvio: String?,
    val notas: String?
)

// ─── Devolución ──────────────────────────────────────

/**
 * Request para firmar la devolución de una renta.
 */
data class DevolucionFirmaRequest(
    val rentaId: Int,
    val fechaDevolucionProgramada: String? = null,
    val fechaDevolucionReal: String? = null,
    val estadoDevolucion: com.example.axxionsystem.data.model.Alquiler.EstadoDevolucion? = null,
    val personaRecibe: String? = null,
    val firmaDigital: String? = null,
    val notasGenerales: String? = null,
    val condicionesRegreso: List<com.example.axxionsystem.data.model.Alquiler.CondicionItemRequest>? = emptyList()
)

/**
 * Response después de firmar devolución.
 */
data class DevolucionResponse(
    val id: Int,
    val rentaId: Int,
    val estado: com.example.axxionsystem.data.model.Alquiler.EstadoDevolucion,
    val fechaDevolucionReal: String?,
    val notasGenerales: String?
)
