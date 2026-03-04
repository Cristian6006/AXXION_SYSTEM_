package com.example.axxionSystem.dto

import com.example.axxionSystem.model.EstadoDevolucion
import com.example.axxionSystem.model.EstadoEntrega
import com.example.axxionSystem.model.EstadoRenta
import com.example.axxionSystem.model.EstadoSolicitud
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

// Solicitudes

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

// Rentas

data class RentaItemRequest(
    val inventarioItemId: Int,
    val precioRentaItem: BigDecimal,
    val condicionSalida: String? = null,
    val notas: String? = null
)

data class RentaCreateRequest(
    val clienteId: Int,
    val cotizacionId: Int? = null,
    val fechaInicio: LocalDateTime,
    val fechaFinPrevista: LocalDateTime,
    val montoTotalRenta: BigDecimal? = null,
    val depositoGarantia: BigDecimal? = null,
    val notas: String? = null,
    val items: List<RentaItemRequest>
)

data class RentaResponse(
    val id: Int,
    val clienteId: Int,
    val estado: EstadoRenta,
    val fechaInicio: LocalDateTime,
    val fechaFinPrevista: LocalDateTime,
    val fechaDevolucionReal: LocalDateTime?,
    val items: List<RentaItemResponse>
)

data class RentaItemResponse(
    val inventarioItemId: Int,
    val productoId: Int,
    val precioRentaItem: BigDecimal,
    val condicionSalida: String?,
    val condicionRegreso: String?,
    val notas: String?
)

// Entrega y Devolucion

data class EntregaFirmaRequest(
    val rentaId: Int,
    val direccionId: Int,
    val fechaEnvio: LocalDateTime? = null,
    val companiaEnvio: String? = null,
    val numeroGuia: String? = null,
    val estadoEntrega: EstadoEntrega? = null,
    val firmaDigital: String? = null,
    val notas: String? = null,
    val condicionesSalida: List<CondicionItemRequest>? = emptyList()
)

data class DevolucionFirmaRequest(
    val rentaId: Int,
    val fechaDevolucionProgramada: LocalDate? = null,
    val fechaDevolucionReal: LocalDateTime? = null,
    val estadoDevolucion: EstadoDevolucion? = null,
    val personaRecibe: String? = null,
    val firmaDigital: String? = null,
    val notasGenerales: String? = null,
    val condicionesRegreso: List<CondicionItemRequest>? = emptyList()
)

data class CondicionItemRequest(
    val inventarioItemId: Int,
    val condicion: String? = null,
    val notas: String? = null
)

data class EntregaResponse(
    val id: Int,
    val rentaId: Int,
    val direccionId: Int,
    val estado: EstadoEntrega,
    val fechaEnvio: LocalDateTime?,
    val notas: String?
)

data class DevolucionResponse(
    val id: Int,
    val rentaId: Int,
    val estado: EstadoDevolucion,
    val fechaDevolucionReal: LocalDateTime?,
    val notasGenerales: String?
)
