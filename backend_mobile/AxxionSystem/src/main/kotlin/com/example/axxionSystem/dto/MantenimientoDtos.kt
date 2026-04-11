package com.example.axxionSystem.dto

import com.example.axxionSystem.model.TipoMantenimiento
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Request para crear una nueva solicitud de mantenimiento.
 * Valida que el ID del inventario sea válido y tenga descripción del problema.
 */
data class MantenimientoCreateRequest(
    val inventarioItemId: Int,
    val descripcionProblema: String? = null,
    val evidenciaFoto: String? = null,
    val tipoMantenimiento: TipoMantenimiento? = TipoMantenimiento.Correctivo,
    val responsable: String? = null,
    val fechaFinPrevista: LocalDate? = null,
    val costoEstimado: BigDecimal? = null
) {
    /**
     * Valida que los campos requeridos estén presentes.
     * @throws IllegalArgumentException si la validación falla.
     */
    fun validate() {
        require(inventarioItemId > 0) { "El ID del item de inventario debe ser mayor a 0" }
        require(descripcionProblema.isNullOrBlank() || descripcionProblema.length <= 500) {
            "La descripción del problema no puede exceder 500 caracteres"
        }
    }
}

/**
 * Request para actualizar una solicitud de mantenimiento existente.
 * Permite actualizar estado, descripción del trabajo, costos y fechas.
 */
data class MantenimientoUpdateRequest(
    val estadoMantenimiento: String? = null,
    val descripcionTrabajoRealizado: String? = null,
    val evidenciaFoto: String? = null,
    val fechaFinReal: LocalDate? = null,
    val costoReal: BigDecimal? = null,
    val responsable: String? = null
) {
    /**
     * Valida los campos de actualización.
     * @throws IllegalArgumentException si la validación falla.
     */
    fun validate() {
        val estadosValidos = listOf("En revision", "En proceso", "Finalizado", "Cancelado")
        if (!estadoMantenimiento.isNullOrBlank()) {
            require(estadosValidos.any { it.equals(estadoMantenimiento, ignoreCase = true) }) {
                "Estado de mantenimiento inválido. Estados válidos: $estadosValidos"
            }
        }
        if (!descripcionTrabajoRealizado.isNullOrBlank()) {
            require(descripcionTrabajoRealizado.length <= 1000) {
                "La descripción del trabajo no puede exceder 1000 caracteres"
            }
        }
        if (costoReal != null) {
            require(costoReal >= BigDecimal.ZERO) { "El costo real no puede ser negativo" }
        }
    }
}

/**
 * Response completo de mantenimiento para el cliente.
 * Incluye información del item de inventario asociado.
 */
data class MantenimientoResponse(
    val id: Int,
    val inventarioItemId: Int,
    val inventarioItemNombre: String? = null,  // Nombre del producto/inventario
    val fechaInicio: LocalDateTime?,
    val fechaFinPrevista: LocalDate?,
    val fechaFinReal: LocalDate?,
    val tipoMantenimiento: String?,
    val estadoMantenimiento: String?,
    val responsable: String?,
    val descripcionProblema: String?,
    val descripcionTrabajoRealizado: String?,
    val costoEstimado: BigDecimal?,
    val costoReal: BigDecimal?,
    val evidenciaFoto: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

/**
 * Request para filtrar mantenimientos por criterios múltiples.
 */
data class MantenimientoFilterRequest(
    val estado: String? = null,
    val responsable: String? = null,
    val fechaInicioDesde: LocalDate? = null,
    val fechaInicioHasta: LocalDate? = null,
    val tipoMantenimiento: TipoMantenimiento? = null
)

/**
 * Response de paginación para mantenimientos.
 */
data class MantenimientoPageResponse(
    val mantenimientos: List<MantenimientoResponse>,
    val total: Int,
    val paginaActual: Int,
    val totalPaginas: Int
)
